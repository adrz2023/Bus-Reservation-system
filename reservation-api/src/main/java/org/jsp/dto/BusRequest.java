package org.jsp.dto;

import java.time.LocalDate;


import lombok.Data;
@Data
public class BusRequest {

	private String name;
	private int bus_number;
	private int seats;
	private String from_location;
	private  String to_location ;
    private LocalDate bus_depurture;
 	

    private String description;
	private String imageUrl;
	 private double costPerSeat;
	
}
