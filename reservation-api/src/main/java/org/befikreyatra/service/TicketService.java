package org.befikreyatra.service;

import java.util.*;


import jakarta.transaction.Transactional;
import org.befikreyatra.dao.*;
import org.befikreyatra.dto.*;
import org.befikreyatra.exception.AdminNotFoundException;
import org.befikreyatra.model.*;
import org.befikreyatra.util.TicketStatus;
import org.befikreyatra.util.TripSeatStatus;
import org.befikreyatra.util.TripStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class TicketService {
	@Autowired
	private BusDao busDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private TicketDao ticketDao;
    @Autowired
    private TripDao tripDao;
    @Autowired
    private TripSeatDao tripSeatDao;


	public ResponseEntity<ResponseStructure<TicketResponse>>bookTicket(int userId, TicketBookingRequest req){
		ResponseStructure<TicketResponse> structure=new ResponseStructure<>();

        User user = userDao.findById(userId)
                .orElseThrow(() -> new AdminNotFoundException("Invalid user"));
        Trip trip = tripDao.findById(req.getTripId())
                .orElseThrow(() -> new AdminNotFoundException("Invalid trip"));

        if (trip.getStatus() != TripStatus.ACTIVE) {
            throw new AdminNotFoundException("Trip is not active");
        }
        int extraCount = 0;
        if (req.getExtraPassengers() != null) {
            extraCount = req.getExtraPassengers().size();
        }
        int seatsBooked = extraCount;
        if (Boolean.TRUE.equals(req.getIncludeSelf())) {
            seatsBooked = seatsBooked + 1;
        }
        if (seatsBooked <= 0) {
            throw new AdminNotFoundException("At least one passenger is required");
        }
        if (trip.getAvailableSeats() < seatsBooked) {
            throw new AdminNotFoundException("Insufficient seats");
        }
        Ticket ticket= new Ticket();
        ticket.setUser(user);
        ticket.setTrip(trip);
        ticket.setBus(trip.getBus());
        ticket.setNumberOfSeatsBooked(seatsBooked);
        ticket.setCost(seatsBooked * trip.getCostPerSeat());
        ticket.setStatus(TicketStatus.BOOKED.toString());
        List<Passenger> passengers = new ArrayList<>();

        if (Boolean.TRUE.equals(req.getIncludeSelf())) {
            Passenger self = new Passenger();
            self.setName(user.getName());
            self.setAge(user.getAge());
            self.setGender(user.getGender());
            self.setTicket(ticket);
            passengers.add(self);
        }

        if (req.getExtraPassengers() != null) {
            for (PassengerRequest p : req.getExtraPassengers()) {
                Passenger group = new Passenger();
                group.setName(p.getName());
                group.setAge(p.getAge());
                group.setGender(p.getGender());
                group.setTicket(ticket);
                passengers.add(group);
            }
        }
        ticket.setPassengers(passengers);
        trip.setAvailableSeats(trip.getAvailableSeats() - seatsBooked);
        ticket = ticketDao.saveTicket(ticket);
        tripDao.saveTrip(trip);

        structure.setData(mapToTicketResponse(ticket, user, trip.getBus()));
        structure.setMessege("Ticket booked successfully");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.ok(structure);
    }


	public ResponseEntity<ResponseStructure<List<TicketResponse>>> findTicketsByUserId(int userId){
		ResponseStructure<List<TicketResponse>> structure = new ResponseStructure<>();

		Optional<User> recUser = userDao.findById(userId);
		if(recUser.isEmpty())
			throw new AdminNotFoundException("User not found");
		User dbUser = recUser.get();

		List<Ticket> tickets = ticketDao.findByUserId(userId);
		List<TicketResponse> responses = new ArrayList<>();

		for (Ticket ticket : tickets) {
			Bus bus = ticket.getBus();
			TicketResponse ticketResponse = mapToTicketResponse(ticket, dbUser, bus);
			responses.add(ticketResponse);
		}
			structure.setData(responses);
			structure.setMessege("Tickets booked by user");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
@Transactional
public ResponseEntity<ResponseStructure<TicketResponse>> reserveSeats(int userId, SeatReserveRequest req){
    ResponseStructure<TicketResponse> structure = new ResponseStructure<>();
    User user = userDao.findById(userId)
            .orElseThrow(() -> new AdminNotFoundException("Invalid user"));
    Trip trip = tripDao.findById(req.getTripId())
            .orElseThrow(() -> new AdminNotFoundException("Invalid trip"));
    if (trip.getStatus() != TripStatus.ACTIVE) {
        throw new AdminNotFoundException("Trip is not active");
    }

    int extraCount;
    if (req.getExtraPassengers() == null) {
        extraCount = 0;
    } else {
        extraCount = req.getExtraPassengers().size();
    }
    int passengerCount = extraCount;

    if (Boolean.TRUE.equals(req.getIncludeSelf())) {
        passengerCount = passengerCount + 1;
    }

    if (passengerCount <= 0) throw new AdminNotFoundException("At least one passenger is required");


    if (req.getSeatCodes() == null || req.getSeatCodes().size() != passengerCount) {
        throw new AdminNotFoundException("Seat selection count must match passenger count");
    }

    Set<String> uniq =new HashSet<>(req.getSeatCodes());
    if (uniq.size() != req.getSeatCodes().size()) {
        throw new AdminNotFoundException("Duplicate seat codes are not allowed");
    }

    List<TripSeat> locked = tripSeatDao.lockSeatsForUpdate(trip.getId(),req.getSeatCodes());
    if (locked.size() != req.getSeatCodes().size()) {
        throw new AdminNotFoundException("One or more selected seats do not exist for this trip");
    }
    for (TripSeat s : locked) {
        if (s.getStatus() != TripSeatStatus.AVAILABLE) {
            throw new AdminNotFoundException("One or more selected seats are not available");
        }
    }
    if (trip.getAvailableSeats() < passengerCount) {
        throw new AdminNotFoundException("Insufficient seats");
    }

    Ticket ticket = new Ticket();
    ticket.setUser(user);
    ticket.setTrip(trip);
    ticket.setBus(trip.getBus());
    ticket.setNumberOfSeatsBooked(passengerCount);
    ticket.setCost(passengerCount * trip.getCostPerSeat());
    ticket.setStatus(TicketStatus.BOOKED.toString());

    List<Passenger> passengers = new ArrayList<>();

    int idx = 0;
    if (Boolean.TRUE.equals(req.getIncludeSelf())) {
        Passenger self = new Passenger();
        self.setName(user.getName());
        self.setAge(user.getAge());
        self.setGender(user.getGender());
        self.setSeatCode(req.getSeatCodes().get(idx++));
        self.setTicket(ticket);
        passengers.add(self);
    }
    if (req.getExtraPassengers() != null) {
        for (PassengerRequest p : req.getExtraPassengers()) {
            Passenger group = new Passenger();
            group.setName(p.getName());
            group.setAge(p.getAge());
            group.setGender(p.getGender());
            group.setSeatCode(req.getSeatCodes().get(idx++));
            group.setTicket(ticket);
            passengers.add(group);
        }
    }
    ticket.setPassengers(passengers);
    ticket = ticketDao.saveTicket(ticket);

    for (TripSeat s : locked) {
        s.setStatus(TripSeatStatus.BOOKED);
        s.setTicket(ticket);
        s.setHeldUntil(null);
    }
    tripSeatDao.saveAll(locked);

    // Update availability (safe: count remaining AVAILABLE)
    int remaining = (int) tripSeatDao.countAvailable(trip.getId());
    trip.setAvailableSeats(remaining);
    tripDao.saveTrip(trip);

    structure.setData(mapToTicketResponse(ticket, user, trip.getBus()));
    structure.setMessege("Ticket booked successfully");
    structure.setStatuscode(HttpStatus.OK.value());
    return ResponseEntity.ok(structure);

}


	private TicketResponse mapToTicketResponse(Ticket ticket, User user, Bus bus) {
		// TODO Auto-generated method stub

		TicketResponse ticketResponse=new TicketResponse();
		 ticketResponse.setAge(user.getAge());
		 ticketResponse.setBusName(bus.getName());
		 ticketResponse.setBus_number(bus.getBus_number());
		 ticketResponse.setCost(ticket.getCost());
		 ticketResponse.setDateOfBooking(ticket.getDateOfBooking());
		 ticketResponse.setGender(user.getGender());
		 ticketResponse.setId(ticket.getId());
		 ticketResponse.setNumberOfSeatsBooked(ticket.getNumberOfSeatsBooked());
		 ticketResponse.setPhone(user.getPhone());
		 ticketResponse.setDescription(bus.getDescription());
		 ticketResponse.setStatus(ticket.getStatus());
		 ticketResponse.setUserName(user.getName());

		return ticketResponse;
	}

}