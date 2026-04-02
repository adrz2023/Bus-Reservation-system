package org.befikreyatra.repository;

import org.befikreyatra.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    List<Review> findByBusId(int BusId);
    Optional<Review> findByBusIdAndUserId(int busId, int userId);
}
