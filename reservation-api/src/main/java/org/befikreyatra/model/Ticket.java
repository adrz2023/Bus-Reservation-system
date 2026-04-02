package org.befikreyatra.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@Entity
public class Ticket {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
   @CreationTimestamp
	private LocalDate dateOfBooking;
	@Column(nullable = false)
	private double cost;
	private String status;
	@Column(nullable = false)
	private int numberOfSeatsBooked;
	@ManyToOne
	@JoinColumn(name="bus_id")
	@JsonIgnore
	private Bus bus;
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonIgnore
	private User user;
	@ManyToOne
	@JoinColumn(name = "trip_id")
	@JsonIgnore
	private Trip trip;
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Passenger> passengers;
}
