package men.brakh.emergencymap.models;

import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.db.EmergenciesRepository;
import men.brakh.emergencymap.db.PopulationsRepository;
import men.brakh.emergencymap.models.сoefficientsСalculators.CoefCalcFactoryMethod;
import men.brakh.emergencymap.models.сoefficientsСalculators.CoefficientsСalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Класс со списом Регионов с их количеством повторений ситуаций и цветом
 */
@Service
public class RegionsList {
    private int n;
    private Date startDate;
    private Date endDate;

    private String mode;

    private CoefficientsСalculator coefficientsСalculators;

    ArrayList<Region> regionsList = new ArrayList<>();

    private static EmergenciesRepository emergenciesRepository;
    private static PopulationsRepository populationsRepository;


    public RegionsList() {

    }

    /**
     * Создание списка регионов
     * @param n Количество ситуаций
     * @param startDate Дата, с которой начинается поиск (в формате yyyy-MM-dd)
     * @param endDate Дата, на которой заканчивается поиск (в формате yyyy-MM-dd)
     */
    public RegionsList(int n, Date startDate, Date endDate, String mode) {
        this.n = n;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mode = mode;
        // Заполняем список
        init();
        // Получаем цвета каждого региона
        initialiseColors();
    }
    @Autowired
    private void setPopulationRepository(PopulationsRepository populationsRepository) {
        this.populationsRepository = populationsRepository;
    }
    @Autowired
    private void setEmergenciesRepository(EmergenciesRepository emergenciesRepository) {
        this.emergenciesRepository = emergenciesRepository;
    }


    /**
     * Возвращаем результат запроса в БД, предварительно превратив из итератора в список
     * @return
     */
    List<Emergencies> getStaticsFromDB() {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByDateRange(startDate, endDate);
        List<Emergencies> target = new ArrayList<>();
        emergencies.forEach(target::add);

        return target;
    }

    /**
     * Инициализация списка из базы данных по параметрам из конструктора
     */
    public void init() {
        // Получаем итерируемый объект со списком строк базы данных по запросу
        List<Emergencies> emergencies = getStaticsFromDB();

        if(emergencies.size() == 0) {
            return; // Если из базы данных ничего не вернули - просто выходим из инициализации
        }

        // Получаем первую вернувшуюся строку
        Emergencies firstEmergency = emergencies.get(0);

        // Получаем первый регион из запроса и создаем объект
        Region currRegion = new Region(firstEmergency.getRegion(), n);

        /* Запрос в БД делается с сортировкой по названию региона =>
            они идут по порядку. Поэтому идем по списку и для регионов с
            одним названием изменяем количество повторений той или инной ситуации
            (берется из БД). Изменение происходит путем инкремента
         */

        for(Emergencies emergency : emergencies) {
            if(currRegion.getName().equals(emergency.getRegion())) {
                currRegion.incSituation(emergency.getSituation() - 1);
            } else {
                regionsList.add(currRegion); // Регионы с таким названием закончились => пушим в список
                currRegion = new Region(emergency.getRegion(), n);
                currRegion.incSituation(emergency.getSituation() - 1);
            }
        }
        regionsList.add(currRegion); // Добавляем последний регион
    }


    /**
     * Получаем цвет определенного региона (Цвет i-го региона получается путем осветление его базового цвета на процент,
     * на который он отличается от максимального показателя для этого региона)
     * @param region Объект региона
     * @param basicColors Базовые цвета ситуаций (при максимальном числе повторений ситуации)
     * @see Color#getBasicColors(int)
     * @param coefficientsСalculators Объект для расчета коэффициента ответления цвета ситуации
     * @see CoefficientsСalculator
     * @return Объект результирующего цвета
     */
    public Color getRegionColor(Region region, Color[] basicColors, CoefficientsСalculator coefficientsСalculators) {
        int[] regionSits = region.getSits();

        LinkedList<Color> colors= new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (regionSits[i] != 0) {
                // Получаем коэффициент "осветления"
                int coef = coefficientsСalculators.calc(region, i);
                // Осветляем коэффициент на coef% и заносим в список
                colors.add(new Color(basicColors[i]).ligherColor(coef));
            }
        }

        // Переносим значения из списка в массив
        Color[] colorMap = new Color[colors.size()];
        for(int i = 0; i < colorMap.length; i++) {
            colorMap[i] = colors.get(i);
        }
        colors.remove();

        return Color.mixColors(colorMap); // Смешиваем цвета из массива и возвращаем результирующий цвет
    }

    /**
     * Получаем цвета для каждого региона
     */
    public void initialiseColors() {
        // Создание фабрики для создания объекта, который будет вычислять коэффициент осветления.
        // Внутри себя объект подсчета коэффициента создает объект подсчета массив максимального числа повторений
        CoefCalcFactoryMethod coefCalcFactoryMethod = new CoefCalcFactoryMethod(regionsList, n);
        coefficientsСalculators = coefCalcFactoryMethod.getCoefficientCalculator(mode);

        Color[] basicColors = Color.getBasicColors(n); // Получаем базовый цвета ситуаций



        for(int i = 0; i < regionsList.size(); i++) {
            Region currRegion = regionsList.get(i);

            // Получаем цвет конкретного региона:
            String regionColor = getRegionColor(currRegion, basicColors, coefficientsСalculators).getHex();

            // Устанавливаем цвет для этого региона
            currRegion.setColor(regionColor);
            regionsList.set(i, currRegion);
        }
    }

    /**
     * Возвращает список регионов
     * @return Список регионов
     */
    public ArrayList<Region> getList() {
        return regionsList;
    }

    public CoefficientsСalculator getCoefficientsСalculators() {
        return coefficientsСalculators;
    }
}

