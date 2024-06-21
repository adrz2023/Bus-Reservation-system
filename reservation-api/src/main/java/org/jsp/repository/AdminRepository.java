package org.jsp.repository;

import java.util.Optional;

import org.jsp.model.Admin;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
	Optional<Admin> findByPhoneAndPassword(long phone, String password);

	Optional<Admin> findByEmailAndPassword(String email, String password);
	
	Optional<Admin> findByEmail(String email);
	
	Optional<Admin> findByToken(String token);
}