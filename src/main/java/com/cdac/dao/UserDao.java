package com.cdac.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.User;
import com.cdac.entities.UserRole;

public interface UserDao extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

    // keep this for signup validations
    boolean existsByEmail(String email);

    // helpful: find by role
    List<User> findByUserRole(UserRole userRole);

}
