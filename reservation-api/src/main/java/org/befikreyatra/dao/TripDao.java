package org.befikreyatra.dao;

import org.befikreyatra.model.Trip;
import org.befikreyatra.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TripDao {

@Autowired
    private TripRepository tripRepository;

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Optional<Trip> findById(int id) {
        return tripRepository.findById(id);
    }

    public List<Trip> findByBusId(int busId) {
        return tripRepository.findByBusId(busId);
    }
}


