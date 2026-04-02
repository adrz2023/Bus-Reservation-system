package org.befikreyatra.controller;


import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.ReviewRequest;
import org.befikreyatra.dto.ReviewResponse;
import org.befikreyatra.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping("/{busId}/{userId}")
    public ResponseEntity<ResponseStructure<ReviewResponse>> upsertReview(
            @PathVariable int busId,
            @PathVariable int userId,
            @RequestBody ReviewRequest request) {
        return reviewService.upsertReview(busId, userId, request);
    }

    @GetMapping("/{busId}")
    public ResponseEntity<ResponseStructure<List<ReviewResponse>>> getReviewsByBus(@PathVariable int busId) {
        return reviewService.getReviewsByBus(busId);
    }
}
