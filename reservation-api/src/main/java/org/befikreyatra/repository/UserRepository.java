package org.befikreyatra.repository;


import java.util.Optional;


import org.befikreyatra.model.User;

import org.springframework.data.repository.CrudRepository;

public interface  UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByPhoneAndPassword(long phone,String password);
	Optional<User> findByEmailAndPassword(String email,String password);
	
}
