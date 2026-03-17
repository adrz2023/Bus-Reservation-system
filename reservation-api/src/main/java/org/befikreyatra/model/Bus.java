package org.befikreyatra.model;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int id;
	@Column(nullable=false)
	
	private String name;
	@Column(nullable=false)
	
	private int bus_number;
	@Column(nullable=false, unique=true)
	
	private int seats;
	@Column(nullable=false)
	
	private String from_location;
     @Column(nullable=false)
	
	private  String to_location ;
     
     private LocalDate bus_depurture;
     
     private int availableSeats;
     
     private double costPerSeat;
 	
     @ManyToOne
     @JoinColumn(name="vendor_id")
     @JsonIgnore
     private Vendor vendor;
     
     @OneToMany(mappedBy = "bus")
 	private List<Ticket> bookedTickets;
     private String description;
 	private String imageUrl;

 
}
