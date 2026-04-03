package org.befikreyatra.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripSeatLayoutEntryDto {
    private String seatCode;
    private int rowIndex;
    private int colIndex;
    private String seatType; // SEAT/EMPTY/DRIVER
    private Integer deck;
    private boolean isBookable;
}