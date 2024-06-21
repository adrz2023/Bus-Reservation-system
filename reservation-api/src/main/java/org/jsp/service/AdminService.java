package org.jsp.service;

import java.util.Optional;

import org.jsp.dao.AdminDao;
import org.jsp.dto.AdminRequest;
import org.jsp.dto.AdminResponse;
import org.jsp.dto.EmailConfiguration;
import org.jsp.dto.ResponseStructure;
import org.jsp.exception.AdminNotFoundException;
import org.jsp.model.Admin;
import org.jsp.util.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminService {

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private ReservationApiMailService mailservice;
	@Autowired
	private LinkGeneratorService linkGeneratorService;
	@Autowired
	private EmailConfiguration emailConfiguration;
	
	public ResponseEntity<ResponseStructure<AdminResponse>> saveAdmin(AdminRequest adminRequest,
			HttpServletRequest request) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Admin admin = mapToAdmin(adminRequest);
		admin.setStatus(AccountStatus.IN_ACTIVE.toString());
		admin = adminDao.saveAdmin(admin);
		String activation_link = linkGeneratorService.getActivationLink(admin, request);
		emailConfiguration.setSubject("Activate Your Account");
		emailConfiguration.setText(
				"Dear Admin Please Activate Your Account by clicking on the following link:" + activation_link);
		emailConfiguration.setToAddress(admin.getEmail());
		structure.setMessege(mailservice.sendMail(emailConfiguration));
		structure.setData(mapToAdminResponse(admin));
		structure.setStatuscode(HttpStatus.CREATED.value());
		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}
	
	
	public ResponseEntity<ResponseStructure<AdminResponse>> update(AdminRequest adminRequest, int id) {
		Optional<Admin> recAdmin = adminDao.findById(id);
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		if (recAdmin.isPresent()) {
			Admin dbAdmin = recAdmin.get();
			dbAdmin.setEmail(adminRequest.getEmail());
			dbAdmin.setGst_number(adminRequest.getGst_number());
			dbAdmin.setName(adminRequest.getName());
			dbAdmin.setPhone(adminRequest.getPhone());
			dbAdmin.setPassword(adminRequest.getPassword());
			dbAdmin.setTravels_name(adminRequest.getTravels_name());
			structure.setData(mapToAdminResponse(adminDao.saveAdmin(dbAdmin)));
			structure.setMessege("Admin updated");
			structure.setStatuscode(HttpStatus.ACCEPTED.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
	}
	
	
	
	public ResponseEntity<ResponseStructure<AdminResponse>> findById(int id) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.findById(id);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessege("Admin found");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	
	
	
	
	
	
	public ResponseEntity<ResponseStructure<AdminResponse>> verify(long phone, String password) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.verify(phone, password);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessege("verification succcessfull");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> verify(String email, String password) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.verify(email, password);
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
		Optional<Admin> dbAdmin = adminDao.findById(id);
		if (dbAdmin.isPresent()) {
			adminDao.delete(id);
			structure.setData("Admin Found");
			structure.setMessege("deleted");
			structure.setStatuscode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
	
		return ResponseEntity.status(HttpStatus.OK).body(structure);
}
	
	 
	private Admin mapToAdmin(AdminRequest adminRequest) {
		return Admin.builder().email(adminRequest.getEmail()).name(adminRequest.getName()).phone(adminRequest.getPhone()).gst_number(adminRequest.getGst_number()).travels_name(adminRequest.getTravels_name()).password(adminRequest.getPassword()).build();
	}
	
	private AdminResponse mapToAdminResponse(Admin admin) {
		return AdminResponse.builder().name(admin.getName()).email(admin.getEmail()).id(admin.getId())
				.gst_number(admin.getGst_number()).phone(admin.getPhone()).travels_name(admin.getTravels_name())
				.password(admin.getPassword()).build();
	}
	
	public String activate(String token) {
		Optional<Admin> recAdmin = adminDao.findByToken(token);
		if (recAdmin.isEmpty())
			throw new AdminNotFoundException("Invalid Token");
		Admin dbAdmin = recAdmin.get();
		dbAdmin.setStatus("ACTIVE");
		dbAdmin.setToken(null);
		adminDao.saveAdmin(dbAdmin);
		return "Your Account has been activated";
	}
	
	
	public String forgotPassword(String email,HttpServletRequest request) {
		Optional<Admin> recAdmin=adminDao.findByEmail(email);
		
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Invalid email");
		Admin admin=recAdmin.get();
		String resetPasswordLink=linkGeneratorService.getResetPasswordLink(admin, request);
		emailConfiguration.setToAddress(email);
		emailConfiguration.setText("please click on the following  link to reset your password");
		emailConfiguration.setSubject("RESET YOUR PASSWORD");
		mailservice.sendMail(emailConfiguration);
		return "reset password link has been sent registered mail id";
	}
	
	public AdminResponse verifyLink(String token) {
		Optional<Admin> recAdmin=adminDao.findByToken(token);
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Link has been expired");
		Admin dbAdmin=recAdmin.get();
		dbAdmin.setToken(null);
		adminDao.saveAdmin(dbAdmin);
		return mapToAdminResponse(dbAdmin);
	}
	
}
