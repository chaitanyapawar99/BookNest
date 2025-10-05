package com.cdac.dto;

import lombok.Data;

@Data

public class BookDTO 
{
	private Long id;
    private String title;
    private String author;
    private Double price;
    private String description;
    private String imagePath;
    private Long sellerId;
    private Long categoryId;
    private boolean available;
    private boolean approved;
}
