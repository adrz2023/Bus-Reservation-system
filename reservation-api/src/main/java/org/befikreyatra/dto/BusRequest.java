package org.befikreyatra.dto;

import java.time.LocalDate;


import lombok.Data;
@Data
public class BusRequest {

	private String name;
	private int bus_number;
	private int seats;
    private String description;
	private String imageUrl;

	
}
