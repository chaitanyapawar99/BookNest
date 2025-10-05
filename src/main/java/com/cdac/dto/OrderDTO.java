package com.cdac.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data

public class OrderDTO extends BaseDTO
{
	private Long id;
    private Long userId;
    private List<CartDTO> items;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;
}
