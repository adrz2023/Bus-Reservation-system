package org.jsp.service;

import org.jsp.dao.SuperAdminDao;
import org.jsp.dao.VendorDao;
import org.jsp.dto.ResponseStructure;
import org.jsp.dto.SuperAdminRequest;
import org.jsp.dto.SuperAdminResponse;
import org.jsp.dto.VendorResponse;
import org.jsp.model.SuperAdmin;
import org.jsp.model.Vendor;
import org.jsp.util.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminService {

    @Autowired
    private SuperAdminDao superAdminDao;

    @Autowired
    private VendorDao vendorDao;

    public ResponseEntity<ResponseStructure<SuperAdminResponse>> saveSuperAdmin(SuperAdminRequest request) {

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


    public ResponseEntity<ResponseStructure<List<VendorResponse>>> getAllVendors() {
        List<Vendor> all = (List<Vendor>) vendorDao.findAll();
        List<VendorResponse> result = new ArrayList<>();
        for (Vendor v : all)
            result.add(mapToAdminResponse(v));
        ResponseStructure<List<VendorResponse>> structure = new ResponseStructure<>();
        structure.setData(result);
        structure.setMessege("All vendors fetched");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }

    public ResponseEntity<ResponseStructure<List<VendorResponse>>> getPendingVendors() {
        List<Vendor> all = (List<Vendor>) vendorDao.findAll();
        List<VendorResponse> pending = new ArrayList<>();
        for (Vendor v : all) {
            if (ApprovalStatus.PENDING_APPROVAL.equals(v.getApprovalStatus()))
                pending.add(mapToAdminResponse(v));
        }
        ResponseStructure<List<VendorResponse>> structure = new ResponseStructure<>();
        structure.setData(pending);
        structure.setMessege("Pending vendors fetched");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }


    public ResponseEntity<ResponseStructure<String>> updateApprovalStatus(int vendorId,
                                                                           ApprovalStatus newStatus) {
        ResponseStructure<String> structure = new ResponseStructure<>();
        Optional<Vendor> recVendor = vendorDao.findById(vendorId);
        if (recVendor.isEmpty()) {
            structure.setData(null);
            structure.setMessege("Vendor not found");
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(structure);
        }
        Vendor vendor = recVendor.get();
        vendor.setApprovalStatus(newStatus);
        vendorDao.saveAdmin(vendor);

        structure.setData("Updated: " + vendor.getEmail() + " -> " + newStatus.name());
        structure.setMessege("Vendor approval status updated");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }


    private VendorResponse mapToAdminResponse(Vendor admin) {
        return VendorResponse.builder().name(admin.getName()).email(admin.getEmail()).id(admin.getId())
                .gst_number(admin.getGst_number()).phone(admin.getPhone()).travels_name(admin.getTravels_name())
                .password(admin.getPassword()).approvalStatus(admin.getApprovalStatus().toString()).build();
    }
}
