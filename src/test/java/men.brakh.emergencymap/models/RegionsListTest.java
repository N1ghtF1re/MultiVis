package men.brakh.emergencymap.models;

import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.models.maxArrCalculators.impl.BasicMaxArrCalculator;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class RegionsListTest {

    private Emergencies generateEmegrency(String name, int sit) {
        Emergencies emergency;
        emergency = new Emergencies();
        emergency.setSituation(sit);
        emergency.setRegion(name);
        return emergency;
    }


    /**
     * Проверка правильности подсчета количества ситуаций регионов
     */
    @Test
    public void situationsRepeatsCalculationTest() {
        ArrayList<Emergencies> emergencies = new ArrayList<>();

        final String name1 = "Тест";
        final String name2 = "Тест2";
        final String name3 = "Тест3";

        emergencies.add(generateEmegrency(name1, 1));
        emergencies.add(generateEmegrency(name1, 2));
        emergencies.add(generateEmegrency(name1, 1));
        emergencies.add(generateEmegrency(name1, 2));
        emergencies.add(generateEmegrency(name1, 2));
        emergencies.add(generateEmegrency(name1, 3));

        // #######################
        emergencies.add(generateEmegrency(name2, 2));
        emergencies.add(generateEmegrency(name2, 3));

        // #######################

        emergencies.add(generateEmegrency(name3, 3));


        RegionsList regions = new RegionsList(3, new Date(1), new Date(1), "basic") {
            @Override
            List<Emergencies> getStaticsFromDB() {
                return emergencies;
            }
        };

        List<Region> regionList = regions.getList();
        assertEquals(regionList.size(), 3);
        Region region = regionList.get(0);
        assertEquals(region.getName(), name1);
        int[] expectedRepeats = new int[] {2,3,1};
        assertArrayEquals(expectedRepeats, region.getSits());

        region = regionList.get(1);
        assertEquals(region.getName(), name2);
        expectedRepeats = new int[] {0,1,1};
        assertArrayEquals(expectedRepeats, region.getSits());

        region = regionList.get(2);
        assertEquals(region.getName(), name3);
        expectedRepeats = new int[] {0,0,1};
        assertArrayEquals(expectedRepeats, region.getSits());
    }

    /**
     * Тест ситуации, когда по запросу возвращается пустая строка
     */
    @Test
    public void emptyListSituationsRepeatCalculationTest() {
        ArrayList<Emergencies> emergencies = new ArrayList<>();

        RegionsList regions = new RegionsList(3, new Date(1), new Date(1), "basic") {
            @Override
            List<Emergencies> getStaticsFromDB() {
                return emergencies;
            }
        };

        List<Region> regionList = regions.getList();
        assertEquals(regionList.size(), 0);
    }

    /**
     * Проверка правильности подсчета количества ситуаций регионов при одном регионе в базе
     */
    @Test
    public void oneElementSituatuibsRepeatsCalculationTest() {
        ArrayList<Emergencies> emergencies = new ArrayList<>();

        final String name1 = "Test";

        Emergencies emergency;
        emergency = new Emergencies();
        emergency.setRegion(name1);
        emergency.setSituation(1);
        emergencies.add(emergency);

        RegionsList regions = new RegionsList(3, new Date(1), new Date(1), "basic") {
            @Override
            List<Emergencies> getStaticsFromDB() {
                return emergencies;
            }
        };

        List<Region> regionList = regions.getList();
        assertEquals(regionList.size(), 1);
        Region region = regionList.get(0);
        assertEquals(region.getName(), name1);
        assertEquals(region.getSitRepeats(0), 1);
        assertEquals(region.getSitRepeats(1), 0);
        assertEquals(region.getSitRepeats(2), 0);
    }


    /**
     * Проверка генерации массива максимальных значений
     */
    @Test
    public void generateMaxArrTest() {
        final String name1 = "Тест";
        final String name2 = "Тест2";
        final String name3 = "Тест3";
        int countOfMax1 = 21;
        int countOfMax2 = 15;

        ArrayList<Emergencies> emergencies = new ArrayList<>();
        // [0] => 2, [1] => 15, [2] => 0
        emergencies.add(generateEmegrency(name1, 1));
        emergencies.add(generateEmegrency(name1, 1));
        for(int i = 0; i < countOfMax2; i++) {
            emergencies.add(generateEmegrency(name1, 2));
        }

        // [0] => 21, [1] => 1, [2] => 0
        for(int i = 0; i < countOfMax1; i++) {
            emergencies.add(generateEmegrency(name2, 1));
        }
        emergencies.add(generateEmegrency(name2, 2));

        // [0] => 20, [1] => 15, [2] => 0
        for(int i = 0; i < countOfMax1-1; i++) {
            emergencies.add(generateEmegrency(name3, 1));
        }
        for(int i = 0; i < countOfMax2; i++) {
            emergencies.add(generateEmegrency(name3, 2));
        }

        RegionsList regions = new RegionsList(3, new Date(1), new Date(1), "basic") {
            @Override
            List<Emergencies> getStaticsFromDB() {
                return emergencies;
            }
        };


        int[] maxArr = ((BasicMaxArrCalculator)regions.getCoefficientsСalculators().getMaxArrCalculator().
                calc(regions.getList(), 3)).getMaxArr();

        int[] expectedMaxArr = new int[] {countOfMax1, countOfMax2, 0};

        assertArrayEquals(maxArr, expectedMaxArr);

    }
}