package org.befikreyatra.dto;


import lombok.Data;

@Data
public class BusSeatTemplateEntryRequest {
    private String seatCode;
    private int rowIndex;
    private int colIndex;
    private String seatType; // SEAT/EMPTY/DRIVER
    private Integer deck; // nullable
    private Boolean isBookable;
}