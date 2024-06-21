package org.jsp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.jsp.dao.AdminDao;
import org.jsp.dao.BusDao;
import org.jsp.dto.BusRequest;
import org.jsp.dto.BusResponse;
import org.jsp.dto.ResponseStructure;
import org.jsp.model.Admin;
import org.jsp.model.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusService {
@Autowired	
private AdminDao adminDao;
@Autowired
private BusDao busDao;

public ResponseEntity<ResponseStructure<BusResponse>> saveBus(BusRequest busRequest,int adminId){
	ResponseStructure<BusResponse> structure= new ResponseStructure<>();
	
	Optional<Admin> optional=adminDao.findById(adminId);
	
	if(optional.isPresent()) {
		Admin ad=optional.get();
		ad.getBuses().add(mapToBus(busRequest));
		Bus bus=mapToBus(busRequest);
		bus.setAvailableSeats(bus.getSeats());
		bus.setAdmin(ad);
		adminDao.saveAdmin(ad);
		structure.setMessege("bus added");
		structure.setStatuscode(HttpStatus.CREATED.value());
		structure.setData(mapToBusResponse(busDao.saveBus(bus)));
		
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}


public ResponseEntity<ResponseStructure<BusResponse>> updateBus(BusRequest busRequest,int bus_id){
	ResponseStructure<BusResponse> structure=new ResponseStructure<>();
	Optional<Bus> resBus=busDao.findById(bus_id);
	if(resBus.isPresent()) {
		Bus dbBus=resBus.get();
		dbBus.setName(busRequest.getName());
		dbBus.setBus_number(busRequest.getBus_number());
		dbBus.setFrom_location(busRequest.getFrom_location());
		dbBus.setTo_location(busRequest.getTo_location());
		dbBus.setSeats(busRequest.getSeats());
		dbBus.setBus_depurture(busRequest.getBus_depurture());
		structure.setMessege("bus updated");
		structure.setData(mapToBusResponse(busDao.saveBus(dbBus)));
		structure.setStatuscode(HttpStatus.ACCEPTED.value());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
	}

	return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
}


public ResponseEntity<ResponseStructure<Bus>> findById(int id){
	ResponseStructure<Bus> structure=new ResponseStructure<>();
	Optional<Bus> recBus=busDao.findById(id);
	
	structure.setData(recBus.get());
	structure.setMessege("Bus Found");
	structure.setStatuscode(HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}
	
public ResponseEntity<ResponseStructure<List<Bus>>> findAll() {
	ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
	structure.setData(busDao.findAll());
	structure.setMessege("List of All Buses");
	structure.setStatuscode(HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

public ResponseEntity<ResponseStructure<List<Bus>>> findBuses(String from_location, String to_location, LocalDate bus_departure) {
	ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
	List<Bus> buses = busDao.FindBuses(from_location, to_location, bus_departure);
		structure.setData(buses);
	structure.setMessege("List of Buses for entered route on this Date");
	structure.setStatuscode(HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(structure);
	
}


public ResponseEntity<ResponseStructure<List<Bus>>> findByAdminId(int admin_id){
	ResponseStructure<List<Bus>> structure =new ResponseStructure<>();
	List<Bus> buses=busDao.finBusesByAdminId(admin_id);
	structure.setData(buses);
	structure.setMessege("List of Buses for entered Amdin id");
	structure.setStatuscode(HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}
	
public ResponseEntity<ResponseStructure<String>> deleteById(int id){
	ResponseStructure<String> structure=new ResponseStructure<>();
	if (busDao.findById(id).isPresent()) {
		busDao.delete(id);
		structure.setMessege("bus found");
		structure.setData("Bus Deleted");
		structure.setStatuscode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

private  Bus mapToBus (BusRequest busRequest) {
	return Bus.builder().
			name(busRequest.getName()).
			seats(busRequest.getSeats())
			.bus_depurture(busRequest.getBus_depurture()).
			bus_number(busRequest.getBus_number()).
			from_location(busRequest.getFrom_location()).
			to_location(busRequest.getTo_location()).
			description(busRequest.getDescription()).
			imageUrl(busRequest.getImageUrl()).
			costPerSeat(busRequest.getCostPerSeat())
			.build();
}


	private BusResponse mapToBusResponse(Bus bus) {
		return BusResponse.builder().id(bus.getId()).name(bus.getName()).bus_depurture(bus.getBus_depurture()).
				bus_number(bus.getBus_number()).from_location(bus.getFrom_location()).
				to_location(bus.getTo_location()).availableSeats(bus.getAvailableSeats()).costPerSeat(bus.getCostPerSeat()).description(bus.getDescription()).imageUrl(bus.getImageUrl())
				.build();
	}

}
