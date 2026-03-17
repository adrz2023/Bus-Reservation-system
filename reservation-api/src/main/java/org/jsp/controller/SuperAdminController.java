package org.jsp.controller;

import org.jsp.dto.*;
import org.jsp.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {


    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping

    public ResponseEntity<ResponseStructure<SuperAdminResponse>> saveSuperAdmin(@RequestBody SuperAdminRequest request) {
        return superAdminService.saveSuperAdmin(request);
    }

    @GetMapping("/vendors")
    public ResponseEntity<ResponseStructure<List<VendorResponse>>> getAllVendors() {
        return superAdminService.getAllVendors();
    }

    @GetMapping("/vendors/pending")
    public ResponseEntity<ResponseStructure<List<VendorResponse>>> getPendingVendors() {
        return superAdminService.getPendingVendors();
    }

    @PutMapping("/vendor/{id}/approval-status")
    public ResponseEntity<ResponseStructure<String>> updateApprovalStatus(@PathVariable int id, @RequestBody VendorApprovalUpdateRequest req) {
        return superAdminService.updateApprovalStatus(id, req.getStatus());
    }
}
