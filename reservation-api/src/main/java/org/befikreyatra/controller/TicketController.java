package org.befikreyatra.controller;

import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.TicketBookingRequest;
import org.befikreyatra.dto.TicketResponse;
import org.befikreyatra.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/trip/{userId}")
    public ResponseEntity<ResponseStructure<TicketResponse>> bookTicket(
            @PathVariable int userId,
            @RequestBody TicketBookingRequest req) {
        return ticketService.bookTicket(userId, req);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseStructure<List<TicketResponse>>> findTicket(@PathVariable int userId) {
        return ticketService.findTicketsByUserId(userId);
    }

}
