package org.jsp.service;

import java.util.Optional;

import org.jsp.dao.UserDao;
import org.jsp.dto.ResponseStructure;
import org.jsp.dto.UserRequest;
import org.jsp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
@Autowired
	private UserDao userDao;

public ResponseEntity<ResponseStructure<User>> saveUser(UserRequest userRequest) {
	ResponseStructure<User> structure = new ResponseStructure<>();
	structure.setMessege("User saved");
	structure.setData(userDao.saveUser(mapToUser(userRequest)));
	structure.setStatuscode(HttpStatus.CREATED.value());
	return ResponseEntity.status(HttpStatus.CREATED).body(structure);
}



public ResponseEntity<ResponseStructure<User>> update(UserRequest userRequest, int id) {
	Optional<User> recUser = userDao.findById(id);
	ResponseStructure<User> structure = new ResponseStructure<>();
	if (recUser.isPresent()) {
		User dbUser = mapToUser(userRequest);
		dbUser.setId(id);
		structure.setData(userDao.saveUser(dbUser));
		structure.setMessege("user updated");
		structure.setStatuscode(HttpStatus.ACCEPTED.value());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
	}
	return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
}



public ResponseEntity<ResponseStructure<User>> findById(int id){
	
	ResponseStructure<User> structure=new ResponseStructure<>();
	
	Optional<User> dbAdmin=userDao.findById(id);
	if(dbAdmin.isPresent()) {
		structure.setData(dbAdmin.get());
		structure.setMessege("user found");
		structure.setStatuscode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

public ResponseEntity<ResponseStructure<User>> verify(long phone,String password){
	ResponseStructure<User> structure=new ResponseStructure<>();
	
	Optional <User> dbAdmin=userDao.verify(phone, password);
	if(dbAdmin.isPresent()) {
		structure.setData(dbAdmin.get());
		structure.setMessege("verification succcessfull");
		structure.setStatuscode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

public ResponseEntity<ResponseStructure<User>>verify(String email,String password){
	ResponseStructure<User> structure=new ResponseStructure<>();
	
	Optional<User> dbAdmin=userDao.verify(email, password);
	if(dbAdmin.isPresent()) {
		
		structure.setData(dbAdmin.get());
		structure.setMessege("verification succcessfull");
		structure.setStatuscode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}
	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

public ResponseEntity<ResponseStructure<String>> delete(int id) {
	ResponseStructure<String> structure = new ResponseStructure<>();
	Optional<User> dbAdmin = userDao.findById(id);
	if (dbAdmin.isPresent()) {
		userDao.delete(id);
		structure.setData("user Found");
		structure.setMessege("deleted");
		structure.setStatuscode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(structure);
	}

	return ResponseEntity.status(HttpStatus.OK).body(structure);
}

private User mapToUser(UserRequest userRequest) {
	return User.builder().email(userRequest.getEmail()).name(userRequest.getName()).phone(userRequest.getPhone())
			.gender(userRequest.getGender()).age(userRequest.getAge()).password(userRequest.getPassword()).build();
}

}
