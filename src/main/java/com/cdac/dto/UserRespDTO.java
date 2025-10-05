package com.cdac.dto;

import java.time.LocalDate;

import com.cdac.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserRespDTO extends BaseDTO{
	private String firstName;
	private String lastName;
	private String email;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	private UserRole userRole;
	
}
