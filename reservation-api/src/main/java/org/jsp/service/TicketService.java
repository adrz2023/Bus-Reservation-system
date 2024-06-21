package org.jsp.service;

import java.util.Optional;


import org.jsp.dao.BusDao;
import org.jsp.dao.TicketDao;
import org.jsp.dao.UserDao;
import org.jsp.dto.ResponseStructure;
import org.jsp.dto.TicketResponse;
import org.jsp.exception.AdminNotFoundException;
import org.jsp.model.Bus;
import org.jsp.model.Ticket;
import org.jsp.model.User;
import org.jsp.util.AccountStatus;
import org.jsp.util.TicketStatus;
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

	
	public ResponseEntity<ResponseStructure<TicketResponse>>bookTicket(int useId,int busId,int numberOfSeats){
		ResponseStructure<TicketResponse> structure=new ResponseStructure<>();
		Optional<Bus> recBus=busDao.findById(busId);
		Optional<User> recUser=userDao.findById(useId);
		if(recBus.isEmpty())
			throw new AdminNotFoundException("Cannot Book Ticket as Bus is invalid");
		if(recUser.isEmpty())
			throw new AdminNotFoundException("Cannot book ticket as user id is invalid");
		User dbUser=recUser.get();
//		if(dbUser.getStatus().equals(AccountStatus.IN_ACTIVE.toString()))
//			throw new AdminNotFoundException("Please Active Your Account, Then Book Ticket");
		Bus dbBus=recBus.get();
		if(dbBus.getAvailableSeats()< numberOfSeats)
			throw new AdminNotFoundException("Insufficent Seat");
		Ticket ticket=new Ticket();
		ticket.setCost(numberOfSeats*dbBus.getCostPerSeat());
		ticket.setStatus(TicketStatus.BOOKED.toString());
		ticket.setBus(dbBus);
		ticket.setUser(dbUser);
		ticket.setNumberOfSeatsBooked(numberOfSeats);
		dbBus.getBookedTickets().add(ticket);
		dbBus.setAvailableSeats(dbBus.getAvailableSeats()-numberOfSeats);
		userDao.saveUser(dbUser);
		busDao.saveBus(dbBus);
		ticket=ticketDao.saveTicket(ticket);
		structure.setData(mapToTicketResponse(ticket, dbUser, dbBus));
		structure.setMessege("Ticket Booking Successfull");
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
		 ticketResponse.setBus_depurture(bus.getBus_depurture());
		 ticketResponse.setFrom_location(bus.getFrom_location());
		 ticketResponse.setTo_location(bus.getTo_location());
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