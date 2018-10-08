package men.brakh.emergencymap.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EmergenciesRepository extends CrudRepository<Emergencies, Integer> {

    // ПОИСК ПО РЕГИОНУ
    @Query("SELECT e FROM Emergencies e where e.region = :region")
    public Iterable<Emergencies> findByRegion(@Param("region") String title);

    // ПОИСК ПО ДИАПАЗОНУ ДАТ
    @Query("SELECT e FROM Emergencies e where date > :startDate AND date < :endDate ORDER BY region")
    public Iterable<Emergencies> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}