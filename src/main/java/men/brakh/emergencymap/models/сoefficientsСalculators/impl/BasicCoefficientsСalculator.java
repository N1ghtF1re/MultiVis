package men.brakh.emergencymap.models.сoefficientsСalculators.impl;

import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.maxArrCalculators.MaxArrCalculator;
import men.brakh.emergencymap.models.maxArrCalculators.impl.BasicMaxArrCalculator;
import men.brakh.emergencymap.models.сoefficientsСalculators.CoefficientsСalculator;

import java.util.List;

/**
 * Осветление ситуации только на основе максимальных значений
 */
public class BasicCoefficientsСalculator implements CoefficientsСalculator {
    MaxArrCalculator maxArrCalculator;

    public BasicCoefficientsСalculator(List<Region> regionList, int n) {
        maxArrCalculator = new BasicMaxArrCalculator().calc(regionList, n);
    }

    @Override
    public int calc(Region region, int sitInd) {
        int[] regionSits = region.getSits();
        int maxArr[] = ((BasicMaxArrCalculator) maxArrCalculator).getMaxArr();
        return Math.round( 100 - ( (float) regionSits[sitInd]  / (float) maxArr[sitInd] ) * 100 );
    }

    @Override
    public MaxArrCalculator getMaxArrCalculator() {
        return maxArrCalculator;
    }

}
