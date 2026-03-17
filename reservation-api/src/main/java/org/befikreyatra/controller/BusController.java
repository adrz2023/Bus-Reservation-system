package org.befikreyatra.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.befikreyatra.dto.BusRequest;
import org.befikreyatra.dto.BusResponse;
import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.model.Bus;
import org.befikreyatra.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
@RequestMapping("api/bus")



public class BusController {
	
	@Autowired
	
	private BusService busService;
	
	@PostMapping("/{vendorId}")
	public ResponseEntity<ResponseStructure<BusResponse>> saveBus(@RequestBody BusRequest busRequest ,@PathVariable(name="vendorId") int vendorId){
		return busService.saveBus(busRequest, vendorId);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<BusResponse>> updateBus(@Valid @RequestBody BusRequest busRequest,@PathVariable(name = "id") int bus_id){
		return busService.updateBus(busRequest, bus_id);	
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Bus>> findById(@PathVariable int id) {
		return busService.findById(id);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bus>>> findAll() {
		return busService.findAll();
	}

	@GetMapping("/find")
	public ResponseEntity<ResponseStructure<List<Bus>>> findBuses(@RequestParam String from_location, @RequestParam String to_location,
			@RequestParam LocalDate bus_depurture) {
		return busService.findBuses(from_location, to_location, bus_depurture);
	}

	@GetMapping("/find/{vendor_id}")
	public ResponseEntity<ResponseStructure<List<Bus>>> findByVendorId(@PathVariable int vendor_id) {
		return busService.findByAdminId(vendor_id);
	}

	
	@GetMapping("/delete/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteById(@PathVariable(value = "id") int id){
		return busService.deleteById(id);
	}

}
