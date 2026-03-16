package org.jsp.dao;

import java.util.List;
import java.util.Optional;

import org.jsp.model.Vendor;
import org.jsp.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VendorDao {
@Autowired

private VendorRepository vendorRepository;

public Vendor saveAdmin(Vendor vendor) {
	return vendorRepository.save(vendor);
}

	public List<Vendor> findAll(){
		return (List<Vendor>) vendorRepository.findAll();
	}

public Optional<Vendor> findById(int id){
	return vendorRepository.findById(id);
}
public Optional<Vendor> findByEmail(String email){
	return vendorRepository.findByEmail(email);
}
public Optional<Vendor> verify(long phone, String password){
	return vendorRepository.findByPhoneAndPassword(phone, password);
}

public Optional<Vendor>verify(String email, String password){
	return vendorRepository.findByEmailAndPassword(email, password);
}

public void delete(int id) {
	vendorRepository.deleteById(id);
}
public Optional<Vendor> findByToken(String token) {
	return vendorRepository.findByToken(token);
}
}
