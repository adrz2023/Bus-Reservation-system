package org.befikreyatra.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TripSeatsResponse {
    private int tripId;
    private int busId;
    private List<TripSeatLayoutEntryDto> layout;   // from BusSeatTemplate
    private List<TripSeatStatusDto> statuses;      // from TripSeat
}