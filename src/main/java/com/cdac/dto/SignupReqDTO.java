package com.cdac.dto;

import java.time.LocalDate;

import com.cdac.entities.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
public class SignupReqDTO{
	
	@NotBlank(message = "first name must be supplied")
	private String firstName;
	@NotBlank(message = "last name must be supplied")
	@Size(min = 4,max=20,message = "invalid last name length")
	private String lastName;
	
	@NotBlank(message = "Email must be supplied")
	@Email(message = "Invalid email format")
	private String email;
	@NotBlank
	@Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})",message = "Invalid password format")
	private String password;	
	@NotNull(message = "user role must be supplied")
	private UserRole userRole;
	@Past(message="Dob must be in past")
	private LocalDate dob;

}
