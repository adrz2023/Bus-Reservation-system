package org.befikreyatra.service;

import jakarta.transaction.Transactional;
import org.befikreyatra.dao.BusDao;
import org.befikreyatra.dao.BusSeatTemplateDao;
import org.befikreyatra.dao.TripDao;
import org.befikreyatra.dao.TripSeatDao;
import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.TripRequest;
import org.befikreyatra.dto.TripResponse;
import org.befikreyatra.model.Bus;
import org.befikreyatra.model.BusSeatTemplate;
import org.befikreyatra.model.Trip;
import org.befikreyatra.model.TripSeat;
import org.befikreyatra.util.TripSeatStatus;
import org.befikreyatra.util.TripStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
@Service
public class TripService {

    @Autowired
    private TripDao tripDao;
    @Autowired
    private BusDao busDao;
    @Autowired
    private BusSeatTemplateDao busSeatTemplateDao;
    @Autowired
    private TripSeatDao tripSeatDao;


@Transactional
public ResponseEntity<ResponseStructure<TripResponse>> craeteTrip(int busId, TripRequest request) {
        ResponseStructure<TripResponse> structure = new ResponseStructure<>();
        Optional<Bus> recBus = busDao.findById(busId);


        long bookableCount = busSeatTemplateDao.countBookableByBusId(busId);
        if (bookableCount <= 0) {
            structure.setMessege("Bus has no seat template. Please configure seat layout first.");
            structure.setStatuscode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(structure);
        }
        if (recBus.isEmpty()) {
            structure.setMessege("Bus not Found");
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(structure);
        }

        Bus bus = recBus.get();
        Trip trip = Trip.builder().
                bus(bus).from_location(request.getFrom_location()).
                to_location(request.getTo_location()).
                departureDate(request.getDepartureDate()).
                availableSeats(bus.getSeats()).
                costPerSeat(request.getCostPerSeat()).
                status(TripStatus.ACTIVE).build();

        trip = tripDao.saveTrip(trip);
        List<BusSeatTemplate> template = busSeatTemplateDao.findByBusId(busId);
        List<TripSeat> tripSeats = new ArrayList<>();
        for (BusSeatTemplate t : template) {
            if (!t.isBookable()) continue;
            TripSeat s = TripSeat.builder()
                    .trip(trip)
                    .seatCode(t.getSeatCode())
                    .status(TripSeatStatus.AVAILABLE)
                    .heldUntil(null)
                    .ticket(null)
                    .build();
            tripSeats.add(s);
        }
        tripSeatDao.saveAll(tripSeats);
        structure.setData(mapToTripResponse(trip));
        structure.setMessege("Trip created successfully");
        structure.setStatuscode(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(structure);
    }


    private TripResponse mapToTripResponse(Trip t) {
        Bus b = t.getBus();
        return TripResponse.builder()
                .id(t.getId())
                .busId(b.getId())
                .busName(b.getName())
                .busNumber(b.getBus_number())
                .from_location(t.getFrom_location())
                .to_location(t.getTo_location())
                .departureDate(t.getDepartureDate())
                .availableSeats(t.getAvailableSeats())
                .costPerSeat(t.getCostPerSeat())
                .status(t.getStatus().name())
                .description(b.getDescription())
                .imageUrl(b.getImageUrl())
                .build();
    }


    public ResponseEntity<ResponseStructure<List<TripResponse>>> getTripByBus(int busId){

        ResponseStructure<List<TripResponse>> structure = new ResponseStructure<>();

        List<Trip> trips =tripDao.findByBusId(busId);
        List<TripResponse> data= new ArrayList<>();
        for(int i=0; i < trips.size();i++) {
            Trip trip = trips.get(i);
            TripResponse response = mapToTripResponse(trip);
            data.add(response);
        }
            structure.setData(data);
            structure.setMessege("Trips by bus fetched");
            structure.setStatuscode(HttpStatus.OK.value());

            return ResponseEntity.ok(structure);

    }

    public ResponseEntity<ResponseStructure<List<TripResponse>>> searchTrips(String from_location,
                                                                             String to_location,
                                                                             LocalDate departureDate) {
        String from = (from_location == null || from_location.isBlank()) ? null : from_location.trim().toLowerCase();
        String to = (to_location == null || to_location.isBlank()) ? null : to_location.trim().toLowerCase();

        List<Trip> trips = tripDao.searchTrips(from, to, departureDate);

        List<TripResponse> data = new ArrayList<>();
        for (Trip t : trips) {
            data.add(mapToTripResponse(t));
        }
        ResponseStructure<List<TripResponse>> structure = new ResponseStructure<>();

        structure.setData(data);
        structure.setMessege("Trips fetched");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.ok(structure);
    }
}
