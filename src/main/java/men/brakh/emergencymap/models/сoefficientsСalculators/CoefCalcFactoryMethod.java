package men.brakh.emergencymap.models.сoefficientsСalculators;

import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.сoefficientsСalculators.impl.BasicCoefficientsСalculator;
import men.brakh.emergencymap.models.сoefficientsСalculators.impl.PopulationCoefficientsСalculator;

import java.security.InvalidParameterException;
import java.util.List;

public class CoefCalcFactoryMethod {
    private List<Region> regionList;
    private int n;
    public CoefCalcFactoryMethod(List<Region> regionList, int n) {
        this.regionList = regionList;
        this.n = n;
    }

    public CoefficientsСalculator getCoefficientCalculator(String mode) {
        if(mode.equals("basic")) return new BasicCoefficientsСalculator(regionList, n);
        if(mode.equals("population")) return  new PopulationCoefficientsСalculator(regionList, n);


        throw new InvalidParameterException("Mode not found");
    }
}
