package org.befikreyatra.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TripRequest {
    private String from_location;
    private String to_location;
    private LocalDate departureDate;
    private Double costPerSeat;
}