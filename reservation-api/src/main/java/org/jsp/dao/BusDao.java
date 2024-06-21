package org.jsp.dao;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import org.jsp.model.Bus;
import org.jsp.repository.BusRepository;
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
	
	
	public List<Bus> finBusesByAdminId (int admin_id){
		
	return busRepository.findBusesByAdminId(admin_id);
		
	}
	
	
	public List<Bus> FindBuses (String to_location,String from_loaction, LocalDate bus_depurture ){
		return busRepository.FindBuses(to_location, from_loaction, bus_depurture);
	}

	public void delete(int id) {
		busRepository.deleteById(id);
	}
}
