package men.brakh.emergencymap.db;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

import org.springframework.data.repository.CrudRepository;

public interface SituationsRepository extends CrudRepository<Situations, Integer> {

}