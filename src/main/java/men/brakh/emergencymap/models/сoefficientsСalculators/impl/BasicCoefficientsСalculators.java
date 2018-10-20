package men.brakh.emergencymap.models.сoefficientsСalculators.impl;

import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.сoefficientsСalculators.CoefficientsСalculators;

/**
 * Осветление ситуации только на основе максимальных значений
 */
public class BasicCoefficientsСalculators implements CoefficientsСalculators {
    @Override
    public int calc(Region region, int[] maxArr, int sitInd) {
        int[] regionSits = region.getSits();
        return Math.round( 100 - ( (float) regionSits[sitInd]  / (float) maxArr[sitInd] ) * 100 );
    }
}
