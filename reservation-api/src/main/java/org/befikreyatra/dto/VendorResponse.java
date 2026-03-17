package org.befikreyatra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorResponse {

   private int id;
	private long phone;
	private String email;
	private String gst_number;
	private String name;
	private String travels_name;
	private String password;
	private String approvalStatus;
	
	
	
}
