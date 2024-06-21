package org.jsp.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class EmailConfiguration {

	private String toAddress;
	private String subject;
	private String text;
	
	
}
