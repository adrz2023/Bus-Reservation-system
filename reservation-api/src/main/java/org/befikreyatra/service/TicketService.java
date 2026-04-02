package org.befikreyatra.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.befikreyatra.dao.BusDao;
import org.befikreyatra.dao.TicketDao;
import org.befikreyatra.dao.TripDao;
import org.befikreyatra.dao.UserDao;
import org.befikreyatra.dto.PassengerRequest;
import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.TicketBookingRequest;
import org.befikreyatra.dto.TicketResponse;
import org.befikreyatra.exception.AdminNotFoundException;
import org.befikreyatra.model.*;
import org.befikreyatra.util.TicketStatus;
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