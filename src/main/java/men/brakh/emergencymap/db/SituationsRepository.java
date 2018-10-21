package men.brakh.emergencymap.db;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SituationsRepository extends CrudRepository<Situations, Integer> {

    @Query("SELECT COUNT(e) FROM Situations e")
    int getSitCount();
}