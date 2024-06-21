package org.jsp.repository;


import java.util.Optional;


import org.jsp.model.User;

import org.springframework.data.repository.CrudRepository;

public interface  UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByPhoneAndPassword(long phone,String password);
	Optional<User> findByEmailAndPassword(String email,String password);
	
}
