package men.brakh.emergencymap.models.сoefficientsСalculators.impl;

import men.brakh.emergencymap.models.Population;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.maxArrCalculators.MaxArrCalculator;
import men.brakh.emergencymap.models.maxArrCalculators.impl.PopulationMaxArrCalculator;
import men.brakh.emergencymap.models.сoefficientsСalculators.CoefficientsСalculator;

import java.util.List;

public class PopulationCoefficientsСalculator implements CoefficientsСalculator {
    private MaxArrCalculator maxArrCalculator;

    public PopulationCoefficientsСalculator(List<Region> regionList, int n) {
        maxArrCalculator = new PopulationMaxArrCalculator().calc(regionList, n);
    }

    @Override
    public int calc(Region region, int sitInd) {
        long population = Population.get(region.getName());

        int[] regionSits = region.getSits();
        double maxArr[] = ((PopulationMaxArrCalculator) maxArrCalculator).getMaxArr();
        return (int) Math.round( 100 - ( ((float) regionSits[sitInd] / (float) population )  / maxArr[sitInd] ) * 100 );
    }

    @Override
    public MaxArrCalculator getMaxArrCalculator() {
        return maxArrCalculator;
    }
}
