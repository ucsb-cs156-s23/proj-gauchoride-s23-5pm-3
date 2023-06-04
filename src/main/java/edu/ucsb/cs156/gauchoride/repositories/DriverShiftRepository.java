package edu.ucsb.cs156.gauchoride.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucsb.cs156.gauchoride.entities.DriverShift;


@Repository
public interface DriverShiftRepository extends CrudRepository<DriverShift, Long> {
    // https://stackoverflow.com/questions/22007341/spring-jpa-selecting-specific-columns
    @Query("SELECT d FROM drivershifts d")
    Iterable<DriverShiftProjection> getAll();
}
