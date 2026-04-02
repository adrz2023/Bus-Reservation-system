package org.befikreyatra.repository;

import org.befikreyatra.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Integer> {

    List<Trip> findByBusId(int busId);

    @Query("""
  select t from Trip t
  where (:from is null or lower(t.from_location) = :from)
    and (:to is null or lower(t.to_location) = :to)
    and (t.departureDate = COALESCE(:departureDate, t.departureDate))
""")
    List<Trip> searchTrips(@Param("from") String from,
                           @Param("to") String to,
                           @Param("departureDate") LocalDate departureDate);
}
