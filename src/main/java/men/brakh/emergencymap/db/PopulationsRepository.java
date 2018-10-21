package men.brakh.emergencymap.db;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PopulationsRepository extends CrudRepository<Populations, Integer> {

    public Populations findFirstByRegion(@Param("region") String region);


}