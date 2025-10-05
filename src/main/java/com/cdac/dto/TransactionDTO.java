package com.cdac.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionDTO 
{
	private Long id;
	@NotBlank(message = "Payment ID is required")
	private String paymentId;
	private Long userId;
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
    private String status;
    private Double amount;
    private Long orderId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime transactionDate;
}
