package org.befikreyatra.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketBookingRequest {
    private int tripId;
    private Boolean includeSelf; // true = logged user also traveling
    private List<PassengerRequest> extraPassengers; // optional
}