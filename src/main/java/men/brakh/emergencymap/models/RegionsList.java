package men.brakh.emergencymap.models;

import com.google.common.collect.Iterables;
import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.db.EmergenciesRepository;

import java.sql.Date;
import java.util.LinkedList;

public class RegionsList {
    private int n;
    private Date startDate;
    private Date endDate;

    LinkedList<Region> regionsList = new LinkedList<>();

    private EmergenciesRepository emergenciesRepository;

    public RegionsList(int n, Date startDate, Date endDate, EmergenciesRepository emergenciesRepository) {
        this.n = n;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergenciesRepository = emergenciesRepository;
        init();
    }

    public void init() {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByDateRange(startDate, endDate);
        Emergencies firstEmergency = Iterables.get(emergencies, 0);
        Region currRegion = new Region(firstEmergency.getRegion(), n);
        for(Emergencies emergency : emergencies) {
            if(currRegion.getName().equals(emergency.getRegion())) {
                currRegion.incSituation(emergency.getSituation() - 1);
            } else {
                regionsList.add(currRegion);
                currRegion = new Region(emergency.getRegion(), n);
                currRegion.incSituation(emergency.getSituation() - 1);
            }
        }

        for(int i = 0; i < regionsList.size(); i++) {
            Region region = regionsList.get(i);
            System.out.println(region.getName() + " ");
            int[] sits = region.getSits();
            for(int j = 0; j < sits.length; j++) {
                System.out.println(j + " " + sits[j]);
            }
        }

        initialiseColors();
    }

    private int[] getMaxArr() {
        if (regionsList.size() == 0) {
            return new int[n];
        }

        int[] maxArr = new int[n];
        for (int i = 0; i < n; i++) {
            maxArr[i] = regionsList.get(0).getSitRepeats(i);
        }

        for (int i = 1; i < regionsList.size(); i++) {
            for (int j = 0; j < n; j++) {
                if (maxArr[j] < regionsList.get(i).getSitRepeats(j)) {
                    maxArr[j] = regionsList.get(i).getSitRepeats(j);
                }
            }
        }
        return maxArr;
    }

    public Color getRegionColor(int[] regionSits, Color[] basicColors, int[] maxArr) {
        Color[] colorMap = new Color[n];
        for (int i = 0; i < n; i++) {
            if (regionSits[i] != 0) {
                int coef = Math.round( 100 - ( regionSits[i]  / maxArr[i] ) * 100 ); // Получаем коэффициент как процент от максимального значенмй
                colorMap[i] = new Color(basicColors[i]).ligherColor(coef); // Осветляем коэффициент на coef %
            }
        }
        return Color.mixColors(colorMap);
    }

    public void initialiseColors() {
        int[] maxArr = getMaxArr();
        Color[] basicColors = Color.getBasicColors(n);
        for(int i = 0; i < regionsList.size(); i++) {
            Region currRegion = regionsList.get(i);
            int[] regionSits = currRegion.getSits();
            currRegion.setColor(getRegionColor(regionSits, basicColors, maxArr));
            regionsList.set(i, currRegion);
        }
    }

}

