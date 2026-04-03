package org.befikreyatra.repository;

import jakarta.persistence.LockModeType;
import org.befikreyatra.model.TripSeat;
import org.befikreyatra.util.TripSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripSeatRepository extends JpaRepository<TripSeat, Integer> {

    @Query("select ts from TripSeat ts where ts.trip.id=?1 order by ts.seatCode")
    List<TripSeat> findByTripId(int tripId);

    @Query("select ts from TripSeat ts where ts.trip.id=?1 and ts.seatCode=?2")
    Optional<TripSeat> findByTripIdAndSeatCode(int tripId, String seatCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ts from TripSeat ts where ts.trip.id=:tripId and ts.seatCode in :seatCodes")
    List<TripSeat> lockSeatsForUpdate(@Param("tripId") int tripId, @Param("seatCodes") List<String> seatCodes);

    @Query("select count(ts) from TripSeat ts where ts.trip.id=?1 and ts.status=?2")
    long countByTripIdAndStatus(int tripId, TripSeatStatus status);
}
