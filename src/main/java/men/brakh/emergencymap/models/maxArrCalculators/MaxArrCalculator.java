package men.brakh.emergencymap.models.maxArrCalculators;

import men.brakh.emergencymap.models.Region;

import java.util.List;

public interface MaxArrCalculator {
    MaxArrCalculator calculate(List<Region> regionsList, int n);
}
