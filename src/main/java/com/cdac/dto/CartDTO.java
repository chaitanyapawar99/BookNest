package com.cdac.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO 
{
	private Long bookId;
	private Long userId;
    private String title;
    private Double price;
    private List<BookDTO> books;
    private int quantity;
}
