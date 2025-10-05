package com.cdac.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.UserRequestDTO;
import com.cdac.dto.UserRespDTO;
import com.cdac.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserRespDTO> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.debug("Fetching profile for user: {}", email);
        UserRespDTO resp = userService.getUserByEmail(email);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserRespDTO> updateCurrentUserProfile(@Valid @RequestBody UserRequestDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.debug("Updating profile for user: {}", email);
        UserRespDTO updated = userService.updateUserByEmail(email, userDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespDTO> getUser(@PathVariable Long id) {
        log.debug("Fetching user with id {}", id);
        UserRespDTO resp = userService.getUserById(id);
        return ResponseEntity.ok(resp);
    }

    /**
     * If you want simple list (no pagination), keep this method signature:
     * @GetMapping public ResponseEntity<List<UserRespDTO>> getAllUsers()
     *
     * OR use pagination (recommended for production):
     */
    @GetMapping
    public ResponseEntity<List<UserRespDTO>> getAllUsers(Pageable pageable) {
        // If your UserService returns a List, you can ignore pageable or add an overload that returns Page
        log.debug("Fetching all users (pageable = {})", pageable);
        List<UserRespDTO> users = userService.getAllUsers(); // consider switching to Page<UserRespDTO>
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRespDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userDTO) {
        log.debug("Updating user id {} with payload {}", id, userDTO);
        UserRespDTO updated = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Deleting user with id {}", id);
        userService.deleteUser(id);
        // 204 No Content is more semantically correct for delete success without body
        return ResponseEntity.noContent().build();
    }
}
