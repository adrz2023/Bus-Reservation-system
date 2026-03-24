package org.befikreyatra.dto;

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
    private String description;
 	private String imageUrl;


}
