package com.cdac.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.BookDao;
import com.cdac.dao.ReviewDao;
import com.cdac.dao.UserDao;
import com.cdac.dto.ReviewDTO;
import com.cdac.entities.Book;
import com.cdac.entities.Review;
import com.cdac.entities.User;
import com.cdac.custom_exceptions.ResourceNotFoundException;
import com.cdac.custom_exceptions.InvalidInputException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final BookDao bookDao;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDTO addReview(Long userId, Long bookId, ReviewDTO reviewDTO) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Optional: validate rating
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new InvalidInputException("Rating must be between 1 and 5");
        }

        // Optional: prevent duplicate review by same user
        // if (!reviewDao.findByUserIdAndBookId(userId, bookId).isEmpty()) {
        //     throw new InvalidInputException("User has already reviewed this book");
        // }

        Review review = new Review();
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setReviewDate(LocalDateTime.now());
        review.setUser(user);
        review.setBook(book);

        Review saved = reviewDao.save(review);
        log.info("Saved review id={} for book={} by user={}", saved.getId(), bookId, userId);
        return modelMapper.map(saved, ReviewDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByBookId(Long bookId) {
        // validate book exists (helps give 404 instead of empty list when id invalid)
        if (!bookDao.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }

        return reviewDao.findByBookId(bookId).stream()
                .map(r -> modelMapper.map(r, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO postReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getUserId() == null || reviewDTO.getBookId() == null) {
            throw new InvalidInputException("userId and bookId must be provided");
        }
        return addReview(reviewDTO.getUserId(), reviewDTO.getBookId(), reviewDTO);
    }
}
