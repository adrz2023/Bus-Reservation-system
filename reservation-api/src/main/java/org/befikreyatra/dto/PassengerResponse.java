package org.befikreyatra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerResponse {
    private int id;
    private String name;
    private int age;
    private String gender;
}