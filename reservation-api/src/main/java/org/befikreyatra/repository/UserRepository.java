package org.befikreyatra.repository;


import java.time.LocalDate;
import java.util.Optional;


import org.befikreyatra.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface  UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByPhoneAndPassword(long phone,String password);
	Optional<User> findByEmailAndPassword(String email,String password);


	@Query("select count(u) from User u")
	long totalUsers();

//	@Query("select count(u) from User u where u.dateOfBooking >= ?1")
//	long usersFrom(LocalDate fromDate);
	
}
