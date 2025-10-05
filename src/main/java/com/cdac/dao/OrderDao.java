package com.cdac.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Order;
import com.cdac.entities.OrderStatus;

public interface OrderDao extends JpaRepository<Order, Long> 
{
	// Find orders by user
    List<Order> findByUserId(Long userId);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // Find orders between two dates
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    // Find orders by user and status
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

}
