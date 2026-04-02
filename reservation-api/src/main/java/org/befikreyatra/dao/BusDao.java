package org.befikreyatra.dao;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import org.befikreyatra.model.Bus;
import org.befikreyatra.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusDao {
	@Autowired
	private BusRepository busRepository;
	
	public Bus saveBus (Bus bus) {
		return busRepository.save(bus);
	}

	
	public Optional<Bus> findById(int id){
		return busRepository.findById(id);
	}

	
	public List<Bus> findAll (){
		return busRepository.findAll();
	}
	
	
	public List<Bus> finBusesByVendorId(int vendor_id){
		
	return busRepository.findBusesByVendorId(vendor_id);
		
	}
	
//
//	public List<Bus> FindBuses (String from_loaction,String to_location, LocalDate bus_depurture ){
//		return busRepository.FindBuses(from_loaction, to_location, bus_depurture);
//	}

	public void delete(int id) {
		busRepository.deleteById(id);
	}
}
