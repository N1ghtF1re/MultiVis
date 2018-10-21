package men.brakh.emergencymap.models.сoefficientsСalculators;

import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.maxArrCalculators.MaxArrCalculator;

/**
 * Расчет процента осветления цвета ситуации
 */
public interface CoefficientsСalculator {
    int calc(Region region, int sitInd);
    MaxArrCalculator getMaxArrCalculator();
}
