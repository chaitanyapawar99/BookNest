package com.cdac.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Review;

public interface ReviewDao extends JpaRepository<Review, Long> 
{
	// All reviews for a specific book
    List<Review> findByBookId(Long bookId);

    // All reviews by a specific user
    List<Review> findByUserId(Long userId);

    // All reviews for a book with a certain rating
    List<Review> findByBookIdAndRating(Long bookId, int rating);

    // All reviews posted within a date range
    List<Review> findByReviewDateBetween(LocalDateTime start, LocalDateTime end);

    // Reviews by user for a specific book
    List<Review> findByUserIdAndBookId(Long userId, Long bookId);
}

