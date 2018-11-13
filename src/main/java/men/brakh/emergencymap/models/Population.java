package men.brakh.emergencymap.models;

import men.brakh.emergencymap.BeansConfiguration;
import men.brakh.emergencymap.db.Populations;
import men.brakh.emergencymap.db.PopulationsRepository;
import men.brakh.emergencymap.http.APICommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Population {
    private static PopulationsRepository populationRepository = null;

    private static Logger logger = LoggerFactory.getLogger(Population.class);
    private static APICommunication apiCommunication;

    @Autowired
    private void setPopulationRepository(PopulationsRepository populationRepository) {
        Population.populationRepository = populationRepository;
    }


    public static long get(String region) {
        Populations populations = populationRepository.findFirstByRegion(region);
        if (populations != null) {
            return populations.getPopulation();
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(BeansConfiguration.class);
        APICommunication apiCommunication = (APICommunication) context.getBean("apiBean");

        long population = apiCommunication.getPopulation(region);

        Populations n = new Populations();
        n.setRegion(region);
        n.setPopulation(population);

        populationRepository.save(n);

        logger.info(String.format("Added new row in table 'populations' with values: %s | %s ", region, population));

        return population;
    }
}
