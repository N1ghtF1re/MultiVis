package men.brakh.emergencymap.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PolygonsRepository extends CrudRepository<Polygons, Integer> {

    Polygons findFirstByRegion(@Param("region") String region);


}