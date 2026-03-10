package org.jsp.controller;

import org.jsp.dto.ResponseStructure;
import org.jsp.dto.SuperAdminRequest;
import org.jsp.dto.SuperAdminResponse;
import org.jsp.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {


    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping

    public ResponseEntity<ResponseStructure<SuperAdminResponse>> saveSuperAdmin(@RequestBody SuperAdminRequest request){
        return superAdminService.saveSuperAdmin(request);
    }


}
