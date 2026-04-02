package org.befikreyatra.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripResponse {
    private int id;
    private int busId;
    private String busName;
    private int busNumber;

    private String from_location;
    private String to_location;
    private LocalDate departureDate;
    private int availableSeats;
    private double costPerSeat;
    private String status;

    private String description;
    private String imageUrl;
}