package org.befikreyatra.repository;

import java.util.List;
import java.util.Optional;

import org.befikreyatra.model.Vendor;

import org.befikreyatra.util.ApprovalStatus;
import org.springframework.data.repository.CrudRepository;

public interface VendorRepository extends CrudRepository<Vendor, Integer> {
	Optional<Vendor> findByPhoneAndPassword(long phone, String password);

	Optional<Vendor> findByEmailAndPassword(String email, String password);
	
	Optional<Vendor> findByEmail(String email);
	
	Optional<Vendor> findByToken(String token);

	List<Vendor> findByApprovalStatus(String approvalStatus);

	long countByApprovalStatus ( ApprovalStatus approvalStatus);
}