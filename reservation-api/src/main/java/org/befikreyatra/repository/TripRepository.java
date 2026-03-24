package org.befikreyatra.repository;

import org.befikreyatra.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Integer> {

    List<Trip> findByBusId(int busId);
}
