package org.befikreyatra.dao;

import org.befikreyatra.model.Review;
import org.befikreyatra.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class ReviewDao {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findByBusId(int busId) {
        return reviewRepository.findByBusId(busId);
    }

    public Optional<Review> findByBusIdAndUserId(int busId, int userId) {
        return reviewRepository.findByBusIdAndUserId(busId, userId);
    }
}
