package men.brakh.emergencymap.models.maxArrCalculators.impl;

import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.maxArrCalculators.MaxArrCalculator;

import java.util.List;

public class BasicMaxArrCalculator implements MaxArrCalculator {
    private int[] maxArr;

    /**
     * Получаем массив из максимальных повторений i-ой ситуации
     * (i-1)-ый элемент - максимальное число повторений ситуации
     * @return Массив из максимальных повторений i-ой ситуации
     */
    @Override
    public MaxArrCalculator calculate(List<Region> regionsList, int n) {
        if (regionsList.size() == 0) {
            maxArr = new int[n];
            return this;
        }

        maxArr = new int[n];
        for (int i = 0; i < n; i++) { // Изначально инициализируем массив значениями первого региона
            maxArr[i] = regionsList.get(0).getSitRepeats(i);
        }

        for (int i = 1; i < regionsList.size(); i++) {
            for (int j = 0; j < n; j++) {
                if (maxArr[j] < regionsList.get(i).getSitRepeats(j)) {
                    maxArr[j] = regionsList.get(i).getSitRepeats(j);
                }
            }
        }

        return this;
    }

    public int[] getMaxArr() {
        return maxArr;
    }
}
