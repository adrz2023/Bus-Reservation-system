package org.jsp.repository;

import java.time.LocalDate;
import java.util.List;

import org.jsp.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusRepository  extends JpaRepository<Bus, Integer>{
	@Query("select b from Bus b where b.admin.id=?1")
	List<Bus> findBusesByAdminId(int id);
	
	
    @Query("select b from Bus b where b.from_location=?1 and b.to_location=?2 and b.bus_depurture=?3")
	List<Bus>FindBuses (String from_location,String to_location,LocalDate bus_depurture);
		
	
	
	
}
