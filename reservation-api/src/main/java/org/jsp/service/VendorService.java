package org.jsp.service;

import java.util.Optional;

import org.jsp.dao.VendorDao;
import org.jsp.dto.VendorRequest;
import org.jsp.dto.VendorResponse;
import org.jsp.dto.EmailConfiguration;
import org.jsp.dto.ResponseStructure;
import org.jsp.exception.AdminNotFoundException;
import org.jsp.model.Vendor;
import org.jsp.util.AccountStatus;
import org.jsp.util.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VendorService {

	@Autowired
	private VendorDao vendorDao;
	@Autowired
	private ReservationApiMailService mailservice;
	@Autowired
	private LinkGeneratorService linkGeneratorService;
	@Autowired
	private EmailConfiguration emailConfiguration;
	
	public ResponseEntity<ResponseStructure<VendorResponse>> saveVendor(VendorRequest adminRequest,
																		HttpServletRequest request) {
		ResponseStructure<VendorResponse> structure = new ResponseStructure<>();
		Vendor vendor = mapToAdmin(adminRequest);
		vendor.setStatus(AccountStatus.IN_ACTIVE.toString());
		vendor.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
		vendor = vendorDao.saveAdmin(vendor);
		String activation_link = linkGeneratorService.getActivationLink(vendor, request);
		emailConfiguration.setSubject("Activate Your Account");
		emailConfiguration.setText(
				"Dear Admin Please Activate Your Account by clicking on the following link:" + activation_link);
		emailConfiguration.setToAddress(vendor.getEmail());
		structure.setMessege(mailservice.sendMail(emailConfiguration));
		structure.setData(mapToAdminResponse(vendor));
		structure.setStatuscode(HttpStatus.CREATED.value());
		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}
	
	
	public ResponseEntity<ResponseStructure<VendorResponse>> update(VendorRequest adminRequest, int id) {
		Optional<Vendor> recAdmin = vendorDao.findById(id);
		ResponseStructure<VendorResponse> structure = new ResponseStructure<>();
		if (recAdmin.isPresent()) {
			Vendor dbAdmin = recAdmin.get();
			dbAdmin.setEmail(adminRequest.getEmail());
			dbAdmin.setGst_number(adminRequest.getGst_number());
			dbAdmin.setName(adminRequest.getName());
			dbAdmin.setPhone(adminRequest.getPhone());
			dbAdmin.setPassword(adminRequest.getPassword());
			dbAdmin.setTravels_name(adminRequest.getTravels_name());
			structure.setData(mapToAdminResponse(vendorDao.saveAdmin(dbAdmin)));
			structure.setMessege("Admin updated");
			structure.setStatuscode(HttpStatus.ACCEPTED.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
	}
	
	
	
	public ResponseEntity<ResponseStructure<VendorResponse>> findById(int id) {
		ResponseStructure<VendorResponse> structure = new ResponseStructure<>();
		Optional<Vendor> dbAdmin = vendorDao.findById(id);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessege("Admin found");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	
	
	
	
	
	
	public ResponseEntity<ResponseStructure<VendorResponse>> verify(long phone, String password) {
		ResponseStructure<VendorResponse> structure = new ResponseStructure<>();
		Optional<Vendor> dbAdmin = vendorDao.verify(phone, password);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessege("verification succcessfull");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	
	public ResponseEntity<ResponseStructure<VendorResponse>> verify(String email, String password) {
		ResponseStructure<VendorResponse> structure = new ResponseStructure<>();
		Optional<Vendor> dbAdmin = vendorDao.verify(email, password);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessege("verification succcessfull");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	
	public ResponseEntity<ResponseStructure<String>> delete(int id) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		Optional<Vendor> dbAdmin = vendorDao.findById(id);
		if (dbAdmin.isPresent()) {
			vendorDao.delete(id);
			structure.setData("Admin Found");
			structure.setMessege("deleted");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
	
		return ResponseEntity.status(HttpStatus.OK).body(structure);
}
	
	 
	private Vendor mapToAdmin(VendorRequest adminRequest) {
		return Vendor.builder().email(adminRequest.getEmail()).name(adminRequest.getName()).phone(adminRequest.getPhone()).gst_number(adminRequest.getGst_number()).travels_name(adminRequest.getTravels_name()).password(adminRequest.getPassword()).build();
	}
	
	private VendorResponse mapToAdminResponse(Vendor admin) {
		return VendorResponse.builder().name(admin.getName()).email(admin.getEmail()).id(admin.getId())
				.gst_number(admin.getGst_number()).phone(admin.getPhone()).travels_name(admin.getTravels_name())
				.password(admin.getPassword()).approvalStatus(admin.getApprovalStatus().toString()).build();
	}
	
	public String activate(String token) {
		Optional<Vendor> recAdmin = vendorDao.findByToken(token);
		if (recAdmin.isEmpty())
			throw new AdminNotFoundException("Invalid Token");
		Vendor dbAdmin = recAdmin.get();
		dbAdmin.setStatus("ACTIVE");
		dbAdmin.setToken(null);
		vendorDao.saveAdmin(dbAdmin);
		return "Your Account has been activated";
	}
	
	
	public String forgotPassword(String email,HttpServletRequest request) {
		Optional<Vendor> recAdmin= vendorDao.findByEmail(email);
		
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Invalid email");
		Vendor admin=recAdmin.get();
		String resetPasswordLink=linkGeneratorService.getResetPasswordLink(admin, request);
		emailConfiguration.setToAddress(email);
		emailConfiguration.setText("please click on the following  link to reset your password");
		emailConfiguration.setSubject("RESET YOUR PASSWORD");
		mailservice.sendMail(emailConfiguration);
		return "reset password link has been sent registered mail id";
	}
	
	public VendorResponse verifyLink(String token) {
		Optional<Vendor> recAdmin= vendorDao.findByToken(token);
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Link has been expired");
		Vendor dbVendor=recAdmin.get();
		dbVendor.setToken(null);
		vendorDao.saveAdmin(dbVendor);
		return mapToAdminResponse(dbVendor);
	}
	
}
