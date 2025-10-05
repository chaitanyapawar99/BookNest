package com.cdac.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Cart;

public interface CartDao extends JpaRepository<Cart, Long> 
{
	// Find cart by user
    Optional<Cart> findByUserId(Long userId);

    // Check if a user already has a cart
    boolean existsByUserId(Long userId);

    // Delete a user's cart
    void deleteByUserId(Long userId);

}
