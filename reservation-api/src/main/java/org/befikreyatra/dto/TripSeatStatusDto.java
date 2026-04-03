package org.befikreyatra.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripSeatStatusDto {
    private String seatCode;
    private String status; // AVAILABLE/HELD/BOOKED
}