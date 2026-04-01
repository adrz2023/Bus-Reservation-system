package org.befikreyatra.dto;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private int id;
    private int busId;
    private int userId;
    private String userName;
    private int rating;
    private String comment;
    private LocalDate createdAt;
}