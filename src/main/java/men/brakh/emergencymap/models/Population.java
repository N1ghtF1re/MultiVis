package men.brakh.emergencymap.models;

import men.brakh.emergencymap.db.Populations;
import men.brakh.emergencymap.db.PopulationsRepository;
import men.brakh.emergencymap.http.NominatimCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Population {
    private static PopulationsRepository populationRepository;

    @Autowired
    private void setPopulationRepository(PopulationsRepository populationRepository) {
        Population.populationRepository = populationRepository;
    }

    public static long get(String region) {
        Populations populations = populationRepository.findFirstByRegion(region);
        if (populations != null) {
            return populations.getPopulation();
        }
        NominatimCommunication nominatimCommunication = new NominatimCommunication();
        long population = nominatimCommunication.getPopulationFromNominatim(region);

        Populations n = new Populations();
        n.setRegion(region);
        n.setPopulation(population);

        populationRepository.save(n);

        return population;
    }
}
