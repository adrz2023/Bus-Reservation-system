package org.jsp.dto;

import lombok.Data;

@Data
public class ResponseStructure <T>{
	private String messege;
	private T data;
	private int statuscode;

}
