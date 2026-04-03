package org.befikreyatra.dao;

import org.befikreyatra.model.TripSeat;
import org.befikreyatra.repository.TripSeatRepository;
import org.befikreyatra.util.TripSeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TripSeatDao {


    @Autowired
    private TripSeatRepository repo;

    public List<TripSeat> findByTripId(int tripId) {
        return repo.findByTripId(tripId);
    }

    public List<TripSeat> saveAll(List<TripSeat> seats) {
        return repo.saveAll(seats);
    }

    public List<TripSeat> lockSeatsForUpdate(int tripId, List<String> seatCodes) {
        return repo.lockSeatsForUpdate(tripId, seatCodes);
    }

    public long countAvailable(int tripId) {
        return repo.countByTripIdAndStatus(tripId, TripSeatStatus.AVAILABLE);
    }
}
