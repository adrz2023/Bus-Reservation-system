package org.jsp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TicketResponse {
	private int id;
	private LocalDate dateOfBooking;
	private double cost;
	private String status;
	 private LocalDate bus_depurture;
	private int numberOfSeatsBooked;
	private String from_location;
	private  String to_location ;
	private String busName;
	private int bus_number;
	private String userName;
	private long phone;
	private int age;
	private String gender;
	private String description;
	
}
