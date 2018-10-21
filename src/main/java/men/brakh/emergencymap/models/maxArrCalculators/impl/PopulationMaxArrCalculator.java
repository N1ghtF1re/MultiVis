package men.brakh.emergencymap.models.maxArrCalculators.impl;

import men.brakh.emergencymap.http.NominatimCommunication;
import men.brakh.emergencymap.models.Population;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.maxArrCalculators.MaxArrCalculator;

import java.util.List;

public class PopulationMaxArrCalculator implements MaxArrCalculator {
    private double[] maxArr;

    /**
     * Получаем массив из максимальных повторений i-ой ситуации с учетом популяции региона
     * (i-1)-ый элемент - максимальное число повторений ситуации
     * @return Массив из максимальных повторений i-ой ситуации, деленный на число жителей региона
     */
    @Override
    public MaxArrCalculator calculate(List<Region> regionsList, final int n) {
        if (regionsList.size() == 0) {
            maxArr = new double[n];
            return this;
        }

        // Ищем число жителей в регионе:
        NominatimCommunication nominatimCommunication = new NominatimCommunication();

        long population; // Количество жителей;

        maxArr = new double[n];
        population =  Population.get(regionsList.get(0).getName());
        for (int i = 0; i < n; i++) { // Изначально инициализируем массив значениями первого региона
            maxArr[i] = (double) regionsList.get(0).getSitRepeats(i) / (double) population;
        }

        for (int i = 1; i < regionsList.size(); i++) {
            population = Population.get(regionsList.get(i).getName());
            for (int j = 0; j < n; j++) {
                double current = (double) regionsList.get(i).getSitRepeats(j) / (double) population;
                if (maxArr[j] < current) {
                    maxArr[j] = current;
                }
            }
        }

        return this;
    }

    public double[] getMaxArr() {
        return maxArr;
    }
}
