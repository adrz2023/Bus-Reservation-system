package org.befikreyatra.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeatReserveRequest {
    private int tripId;
    private Boolean includeSelf;
    private List<PassengerRequest> extraPassengers;
    private List<String> seatCodes;
}