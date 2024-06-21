package org.jsp.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)

	private int id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false,unique=true)
	private String gender;
	
	private int age;
	@Column(nullable=false)
	private long phone;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String password;

	private String token;
	
	private String status;
	
	@OneToMany(mappedBy = "user")
	private List<Ticket> tickets;

}
