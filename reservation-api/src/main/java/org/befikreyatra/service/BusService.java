package org.befikreyatra.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.befikreyatra.dao.VendorDao;
import org.befikreyatra.dao.BusDao;
import org.befikreyatra.dto.BusRequest;
import org.befikreyatra.dto.BusResponse;
import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.model.Vendor;
import org.befikreyatra.model.Bus;
import org.befikreyatra.util.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusService {
@Autowired	
private VendorDao adminDao;
@Autowired
private BusDao busDao;

public ResponseEntity<ResponseStructure<BusResponse>> saveBus(BusRequest busRequest,int vendorId){
	ResponseStructure<BusResponse> structure= new ResponseStructure<>();
	
	Optional<Vendor> optional=adminDao.findById(vendorId);
	
	if(optional.isPresent()) {
		Vendor ad=optional.get();

		if(ad.getApprovalStatus() != ApprovalStatus.APPROVED){
			structure.setMessege("Vendor is not approved yet");
			structure.setStatuscode(HttpStatus.FORBIDDEN.value());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(structure);
		}
		ad.getBuses().add(mapToBus(busRequest));
		Bus bus=mapToBus(busRequest);
		bus.setVendor(ad);
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
		dbBus.setSeats(busRequest.getSeats());
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

//public ResponseEntity<ResponseStructure<List<Bus>>> findBuses(String from_location, String to_location, LocalDate bus_departure) {
//	ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
//	List<Bus> buses = busDao.FindBuses(from_location, to_location, bus_departure);
//		structure.setData(buses);
//	structure.setMessege("List of Buses for entered route on this Date");
//	structure.setStatuscode(HttpStatus.OK.value());
//	return ResponseEntity.status(HttpStatus.OK).body(structure);
//
//}


public ResponseEntity<ResponseStructure<List<Bus>>> findByAdminId(int vendor_id){
	ResponseStructure<List<Bus>> structure =new ResponseStructure<>();
	List<Bus> buses=busDao.finBusesByVendorId(vendor_id);
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
			seats(busRequest.getSeats()).
			bus_number(busRequest.getBus_number()).
			description(busRequest.getDescription()).
			imageUrl(busRequest.getImageUrl())
			.build();
}


	private BusResponse mapToBusResponse(Bus bus) {
		return BusResponse.builder().id(bus.getId()).name(bus.getName()).description(bus.getDescription()).imageUrl(bus.getImageUrl())
				.build();
	}

}
