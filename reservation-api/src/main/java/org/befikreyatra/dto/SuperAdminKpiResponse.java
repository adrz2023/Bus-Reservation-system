package org.befikreyatra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuperAdminKpiResponse {
    private long totalUsers;
    private long newUsersLast7Days;

    private long totalTickets;
    private long ticketsLast7Days;

    private double totalRevenue;
    private double revenueLast7Days;

    private long cancelledTickets;
    private double cancellationRate;

    private long vendorsTotal;
    private long vendorsApproved;
    private long vendorsRejected;
    private long vendorsUnderScreening;
    private long vendorsDocumentReview;
}