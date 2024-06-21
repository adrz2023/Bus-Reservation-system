package org.jsp.controller;

import org.jsp.dto.ResponseStructure;
import org.jsp.dto.TicketResponse;
import org.jsp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/api/ticket")
public class TicketController {
@Autowired
private TicketService ticketService;
@PostMapping("/{userId}/{busId}/{numberOfSeats}")
public ResponseEntity<ResponseStructure< TicketResponse>> bookTicket(@PathVariable int userId,@PathVariable int busId,@PathVariable int numberOfSeats) {
	return ticketService.bookTicket(userId, busId, numberOfSeats);
}
}
