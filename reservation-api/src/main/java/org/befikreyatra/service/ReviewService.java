package org.befikreyatra.service;

import org.befikreyatra.dao.BusDao;
import org.befikreyatra.dao.ReviewDao;
import org.befikreyatra.dao.UserDao;
import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.ReviewRequest;
import org.befikreyatra.dto.ReviewResponse;
import org.befikreyatra.exception.AdminNotFoundException;
import org.befikreyatra.model.Bus;
import org.befikreyatra.model.Review;
import org.befikreyatra.model.User;
import org.befikreyatra.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ReviewService {

    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private BusDao busDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketRepository ticketRepository;


    public ResponseEntity<ResponseStructure<ReviewResponse>> upsertReview(int busId, int userId, ReviewRequest request) {
        ResponseStructure<ReviewResponse> structure = new ResponseStructure<>();

        if (request.getRating() < 1 || request.getRating() > 5) {
            structure.setMessege("Rating must be between 1 and 5");
            structure.setStatuscode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(structure);
        }

        Bus bus = busDao.findById(busId)
                .orElseThrow(() -> new AdminNotFoundException("Invalid bus id"));

        User user = userDao.findById(userId)
                .orElseThrow(() -> new AdminNotFoundException("Invalid user id"));

        boolean eligible = ticketRepository.hasExpiredTicketForBus(userId, busId);
        if (!eligible) {
            structure.setMessege("You can review only after travel completion");
            structure.setStatuscode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(structure);
        }

        Optional<Review> rec = reviewDao.findByBusIdAndUserId(busId, userId);

        Review review;
        if (rec.isPresent()) {
            review = rec.get(); // existing review
        } else {
            review = new Review(); // new review
        }

        review.setBus(bus);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        review = reviewDao.save(review);

        structure.setData(mapToResponse(review));
        structure.setMessege(rec.isPresent() ? "Review updated" : "Review created");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.ok(structure);
    }

    public ResponseEntity<ResponseStructure<List<ReviewResponse>>> getReviewsByBus(int busId) {
        ResponseStructure<List<ReviewResponse>> structure = new ResponseStructure<>();

        List<ReviewResponse> list = new ArrayList<>();
        for (Review r : reviewDao.findByBusId(busId)) {
            list.add(mapToResponse(r));
        }

        structure.setData(list);
        structure.setMessege("Bus reviews fetched");
        structure.setStatuscode(HttpStatus.OK.value());
        return ResponseEntity.ok(structure);
    }


    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .busId(review.getBus().getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
