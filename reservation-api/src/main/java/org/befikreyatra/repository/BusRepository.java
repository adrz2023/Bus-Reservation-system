package org.befikreyatra.repository;

import java.time.LocalDate;
import java.util.List;

import org.befikreyatra.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusRepository  extends JpaRepository<Bus, Integer>{
	@Query("select b from Bus b where b.vendor.id=?1")
	List<Bus> findBusesByVendorId(int id);
	
	
//    @Query("select b from Bus b where b.from_location=?1 and b.to_location=?2 and b.bus_depurture=?3")
//	List<Bus>FindBuses (String from_location,String to_location,LocalDate bus_depurture);
//
//
//
	
}
