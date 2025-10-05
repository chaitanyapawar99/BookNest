package com.cdac.service;

import java.util.List;

import com.cdac.dto.ReviewDTO;

public interface ReviewService 
{
	ReviewDTO addReview(Long userId, Long bookId, ReviewDTO reviewDTO);
    List<ReviewDTO> getReviewsByBookId(Long bookId);
    ReviewDTO postReview(ReviewDTO reviewDTO);
}
