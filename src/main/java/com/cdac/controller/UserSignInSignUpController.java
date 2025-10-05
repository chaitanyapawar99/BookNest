package com.cdac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cdac.dto.AuthResp;
import com.cdac.dto.SignInDTO;
import com.cdac.dto.SignupReqDTO;
import com.cdac.dto.UserRespDTO;
import com.cdac.security.JwtUtils;
import com.cdac.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@Slf4j
public class UserSignInSignUpController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(description = "User sign in")
    public ResponseEntity<?> userSignIn(@RequestBody @Valid SignInDTO dto) {
        log.info("SignIn attempt for: {}", dto.getEmail());
        Authentication authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        try {
            Authentication validAuth = authenticationManager.authenticate(authToken);
            String token = jwtUtils.generateJwtToken(validAuth);
            return ResponseEntity.ok(new AuthResp("Successful login!", token)); // 200 OK
        } catch (AuthenticationException ex) {
            // Let GlobalExceptionHandler handle it (or rethrow a custom AuthenticationException)
            throw ex;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupReqDTO dto) {
        log.info("Signup attempt for: {} with role: {}", dto.getEmail(), dto.getUserRole());
        try {
            UserRespDTO response = userService.signUp(dto);
            log.info("Signup successful for: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Signup failed for: {} - Error: {}", dto.getEmail(), e.getMessage(), e);
            throw e;
        }
    }
}
