package org.jsp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusResponse {
	private int id;
	private String name;
	private int bus_number;
	private int seats;
	private String from_location;
	private  String to_location ;
    private LocalDate bus_depurture;
    private int availableSeats;
    private double costPerSeat;
    private String description;
 	private String imageUrl;


}
