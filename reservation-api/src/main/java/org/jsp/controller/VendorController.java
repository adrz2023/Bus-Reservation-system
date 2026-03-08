package org.jsp.controller;

import java.io.IOException;

import org.jsp.dto.VendorRequest;
import org.jsp.dto.VendorResponse;
import org.jsp.dto.ResponseStructure;

import org.jsp.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class VendorController {
@Autowired
private VendorService vendorService;
@PostMapping
public ResponseEntity<ResponseStructure<VendorResponse>> saveAdmin(@Valid @RequestBody VendorRequest vendorRequest,
																   HttpServletRequest request) {
	return vendorService.saveVendor(vendorRequest, request);
}

@PutMapping("/{id}")
public ResponseEntity<ResponseStructure<VendorResponse>> updateAdmin(@RequestBody VendorRequest vendorRequest,
																	 @PathVariable int id) {
	return vendorService.update(vendorRequest, id);
}

@GetMapping("{id}")
public ResponseEntity<ResponseStructure<VendorResponse>> saveAdmin(@PathVariable int id) {
	return vendorService.findById(id);
}

@PostMapping("/verifyByPhone")
public ResponseEntity<ResponseStructure<VendorResponse>> verify(@RequestParam long phone,
																@RequestParam String password) {
	return vendorService.verify(phone, password);
}

@PostMapping("/verifyByEmail")
public ResponseEntity<ResponseStructure<VendorResponse>> verify(@RequestParam String email,
																@RequestParam String password) {
	return vendorService.verify(email, password);
}

@DeleteMapping("/{id}")
public ResponseEntity<ResponseStructure<String>> delete(@PathVariable int id) {
	return vendorService.delete(id);
}

@GetMapping("/activate")
public String activate(@RequestParam String token) {
	return vendorService.activate(token);
}


@PostMapping("/forgot-password")
public String forgotPassword(@RequestParam String email,HttpServletRequest request) {
	return vendorService.forgotPassword(email, request);
}

@GetMapping("/verify-link")
public void verifyResetPasswordLink(@RequestParam String token,HttpServletResponse response) {
	VendorResponse vendorResponse= vendorService.verifyLink(token);
	if(vendorResponse !=null)
		try {
			response.sendRedirect("http://localhost:3000/reset-password");
		}
	catch (IOException e) {
		e.printStackTrace();
	}
}

}