package org.jsp.service;

import org.jsp.dao.SuperAdminDao;
import org.jsp.dto.ResponseStructure;
import org.jsp.dto.SuperAdminRequest;
import org.jsp.dto.SuperAdminResponse;
import org.jsp.model.SuperAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminService {

    @Autowired
    private SuperAdminDao superAdminDao;

   public ResponseEntity<ResponseStructure<SuperAdminResponse>> saveSuperAdmin(SuperAdminRequest request){

       ResponseStructure<SuperAdminResponse> structure = new ResponseStructure<>();

       SuperAdmin superAdmin = SuperAdmin.builder()
               .name(request.getName())
               .email(request.getEmail())
               .password(request.getPassword())
               .build();

       superAdmin = superAdminDao.save(superAdmin);

       SuperAdminResponse response = SuperAdminResponse.builder()
               .id(superAdmin.getId())
               .name(superAdmin.getName())
               .email(superAdmin.getEmail())
               .build();

       structure.setData(response);
       structure.setMessege("Super Admin Registered Successfully");
       structure.setStatuscode(HttpStatus.CREATED.value());

       return ResponseEntity.status(HttpStatus.CREATED).body(structure);
   }

}
