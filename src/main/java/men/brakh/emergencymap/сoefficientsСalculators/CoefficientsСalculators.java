package men.brakh.emergencymap.сoefficientsСalculators;

import men.brakh.emergencymap.models.Region;

/**
 * Расчет процента осветления цвета ситуации
 */
public interface CoefficientsСalculators {
    public int calc(Region region, int[] maxArr, int sitInd);
}
