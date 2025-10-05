package com.cdac.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data

public class ReviewDTO extends BaseDTO
{
	private Long id;
    private String comment;
    private int rating;
    private Long userId;
    private Long bookId;
    private LocalDateTime reviewDate;
}
