package org.befikreyatra.dto;

import lombok.Data;
import org.befikreyatra.util.ApprovalStatus;
@Data
public class VendorApprovalUpdateRequest {
    private ApprovalStatus status;

}
