package org.jsp.repository;

import java.util.Optional;

import org.jsp.model.Vendor;

import org.springframework.data.repository.CrudRepository;

public interface VendorRepository extends CrudRepository<Vendor, Integer> {
	Optional<Vendor> findByPhoneAndPassword(long phone, String password);

	Optional<Vendor> findByEmailAndPassword(String email, String password);
	
	Optional<Vendor> findByEmail(String email);
	
	Optional<Vendor> findByToken(String token);
}