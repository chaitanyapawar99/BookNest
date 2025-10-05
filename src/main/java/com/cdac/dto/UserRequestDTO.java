package com.cdac.dto;

import java.time.LocalDate;

import com.cdac.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "first name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "invalid length of firstname")
    private String firstName;

    @NotBlank(message = "last name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "invalid length of lastname")
    private String lastName;

    @NotBlank
    @Email(message = "invalid email format")
    private String email;

    // Make password write-only so it won't be serialized in responses
    @NotBlank(message = "password is required")
    @Pattern(
        regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})",
        message = "Password must be 5-20 chars, include 1 digit, 1 lowercase and 1 special char (#@$*)"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Past(message = "invalid date")
    private LocalDate dob;

    @NotNull(message = "user role must be supplied")
    private UserRole userRole;
}
