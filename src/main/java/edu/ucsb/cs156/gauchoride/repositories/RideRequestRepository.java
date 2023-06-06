package edu.ucsb.cs156.gauchoride.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucsb.cs156.gauchoride.entities.RideRequest;

import java.util.Optional;

@Repository
public interface RideRequestRepository extends CrudRepository<RideRequest, Long> {
  Iterable<RideRequest> findAllByRiderId(Long riderId);
}
