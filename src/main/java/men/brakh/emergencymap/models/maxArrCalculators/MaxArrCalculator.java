package men.brakh.emergencymap.models.maxArrCalculators;

import men.brakh.emergencymap.models.Region;

import java.util.List;

public interface MaxArrCalculator {
    MaxArrCalculator calc(List<Region> regionsList, int n);
}
