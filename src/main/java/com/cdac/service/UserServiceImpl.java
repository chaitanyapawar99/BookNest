package com.cdac.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.custom_exceptions.ApiException;
import com.cdac.dao.UserDao;
import com.cdac.dto.SignupReqDTO;
import com.cdac.dto.UserRequestDTO;
import com.cdac.dto.UserRespDTO;
import com.cdac.entities.User;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    // Dependencies
    private final UserDao userDao;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRespDTO signUp(SignupReqDTO dto) {
        // 1. Check for duplicate email
        if (userDao.existsByEmail(dto.getEmail().toLowerCase().trim()))
            throw new ApiException("Duplicate email detected - User already exists!", null);

        // 2. Map DTO to entity
        User entity = mapper.map(dto, User.class);

        // 3. Encrypt password
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        // 4. Save and map back to response DTO
        return mapper.map(userDao.save(entity), UserRespDTO.class);
    }

    @Override
    public UserRespDTO getUserById(Long id) {
        return userDao.findById(id)
                .map(user -> mapper.map(user, UserRespDTO.class))
                .orElseThrow(() -> new ApiException("User not found with id " + id, null));
    }

    @Override
    public UserRespDTO getUserByEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        return userDao.findByEmail(normalizedEmail)
                .map(user -> mapper.map(user, UserRespDTO.class))
                .orElseThrow(() -> new ApiException("User not found with email " + normalizedEmail, null));
    }

    @Override
    public List<UserRespDTO> getAllUsers() {
        return userDao.findAll().stream()
                .map(user -> mapper.map(user, UserRespDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserRespDTO updateUser(Long id, UserRequestDTO userDTO) {
        User existingUser = userDao.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id " + id, null));

        // Check if email change conflicts with existing user
        if (!existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail())
                && userDao.existsByEmail(userDTO.getEmail().toLowerCase().trim())) {
            throw new ApiException("Email already in use by another user", null);
        }

        // Update allowed fields
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setEmail(userDTO.getEmail().toLowerCase().trim());

        // If password is provided, encode it before saving
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userDao.save(existingUser);
        return mapper.map(existingUser, UserRespDTO.class);
    }

    @Override
    public UserRespDTO updateUserByEmail(String email, UserRequestDTO userDTO) {
        String normalizedEmail = email.toLowerCase().trim();
        User existingUser = userDao.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ApiException("User not found with email " + normalizedEmail, null));

        // Check if email change conflicts with existing user
        if (!existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail())
                && userDao.existsByEmail(userDTO.getEmail().toLowerCase().trim())) {
            throw new ApiException("Email already in use by another user", null);
        }

        // Update allowed fields
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setEmail(userDTO.getEmail().toLowerCase().trim());

        // If password is provided, encode it before saving
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userDao.save(existingUser);
        return mapper.map(existingUser, UserRespDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userDao.existsById(id)) {
            throw new ApiException("User not found with id " + id, null);
        }
        userDao.deleteById(id);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id " + id, null));
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }
}
