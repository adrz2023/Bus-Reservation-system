package org.jsp.dto;

import lombok.Data;
import org.jsp.util.ApprovalStatus;
@Data
public class VendorApprovalUpdateRequest {
    private ApprovalStatus status;

}
