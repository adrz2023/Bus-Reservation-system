package org.befikreyatra.service;

import org.befikreyatra.dao.SuperAdminDao;
import org.befikreyatra.dao.VendorDao;
import org.befikreyatra.dto.*;
import org.befikreyatra.model.SuperAdmin;
import org.befikreyatra.model.Vendor;
import org.befikreyatra.repository.TicketRepository;
import org.befikreyatra.repository.UserRepository;
import org.befikreyatra.repository.VendorRepository;
import org.befikreyatra.util.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminService {

    @Autowired
    private SuperAdminDao superAdminDao;

    @Autowired
    private VendorDao vendorDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VendorRepository vendorRepository;

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


    public ResponseEntity<ResponseStructure<SuperAdminKpiResponse>> getDashboardKpis(){
        ResponseStructure<SuperAdminKpiResponse> structure= new ResponseStructure<>();
        LocalDate from7Days = LocalDate.now().minusDays(7);

        long totalUsers = userRepository.totalUsers();
//        long newUsers7 = userRepository.usersFrom(from7Days); // requires createdAt-like field

        long totalTickets = ticketRepository.totalTickets();
        long tickets7 = ticketRepository.ticketsFrom(from7Days);

        double totalRevenue = ticketRepository.totalRevenue();
        double revenue7 = ticketRepository.revenueFrom(from7Days);

        long cancelledTotal = ticketRepository.cancelledTickets();

        double cancellationRate = totalTickets == 0 ? 0.0 : (cancelledTotal * 100.0) / totalTickets;

        long vendorsTotal = (long) vendorDao.findAll().spliterator().getExactSizeIfKnown(); // or vendorRepository.count()
        long approved = vendorRepository.countByApprovalStatus(ApprovalStatus.APPROVED);
        long rejected = vendorRepository.countByApprovalStatus(ApprovalStatus.REJECT);
        long underScreening = vendorRepository.countByApprovalStatus(ApprovalStatus.UNDER_SCREENING);
        long documentReview = vendorRepository.countByApprovalStatus(ApprovalStatus.DOCUMENT_REVIEW);

        SuperAdminKpiResponse kpi = mapToKpiResponse(
                totalUsers , totalTickets, tickets7,
                totalRevenue, revenue7, cancelledTotal, cancellationRate,
                vendorsTotal, approved, rejected, underScreening, documentReview
        );

        structure.setData(kpi);
        structure.setMessege("Super admin KPIs");
        structure.setStatuscode(HttpStatus.OK.value());

        return ResponseEntity.ok(structure);
    }

    private VendorResponse mapToAdminResponse(Vendor admin) {
        return VendorResponse.builder().name(admin.getName()).email(admin.getEmail()).id(admin.getId())
                .gst_number(admin.getGst_number()).phone(admin.getPhone()).travels_name(admin.getTravels_name())
                .password(admin.getPassword()).approvalStatus(admin.getApprovalStatus().toString()).build();
    }


    private SuperAdminKpiResponse mapToKpiResponse(
            long totalUsers,
            long totalTickets,
            long tickets7,
            double totalRevenue,
            double revenue7,
            long cancelledTotal,
            double cancellationRate,
            long vendorsTotal,
            long approved,
            long rejected,
            long underScreening,
            long documentReview) {

        return SuperAdminKpiResponse.builder()
                .totalUsers(totalUsers)
                .totalTickets(totalTickets)
                .ticketsLast7Days(tickets7)
                .totalRevenue(totalRevenue)
                .revenueLast7Days(revenue7)
                .cancelledTickets(cancelledTotal)
                .cancellationRate(cancellationRate)
                .vendorsTotal(vendorsTotal)
                .vendorsApproved(approved)
                .vendorsRejected(rejected)
                .vendorsUnderScreening(underScreening)
                .vendorsDocumentReview(documentReview)
                .build();
    }
}
