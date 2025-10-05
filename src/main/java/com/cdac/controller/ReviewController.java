package com.cdac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.ReviewDTO;
import com.cdac.service.ReviewService;
import com.cdac.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ReviewController 
{
	private final ReviewService reviewService;
	private final UserService userService;

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/books/{bookId}/reviews")
	public ResponseEntity<ReviewDTO> createReview(@PathVariable Long bookId, @RequestBody ReviewDTO reviewDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Long userId = userService.getUserByEmail(email).getId();
		
		ReviewDTO savedReview = reviewService.addReview(userId, bookId, reviewDTO);
		return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
	}

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getBookReviews(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }

    // Legacy endpoints for backward compatibility
    @PreAuthorize("hasRole('USER')")
	@PostMapping("/reviews")
	public ResponseEntity<ReviewDTO> postReview(@RequestBody ReviewDTO reviewDTO) {
	    ReviewDTO savedReview = reviewService.postReview(reviewDTO);
	    return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
	}

    @GetMapping("/reviews/book/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }
}
