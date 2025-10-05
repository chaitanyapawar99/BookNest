package com.cdac.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.CreateOrderRequest;
import com.cdac.dto.OrderDTO;
import com.cdac.service.OrderService;
import com.cdac.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController 
{
	private final OrderService orderService;
	private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getCurrentUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        orderService.placeOrder(userId);
        return ResponseEntity.ok("Order placed successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        // TODO: Add ownership verification to ensure user can only access their own orders
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Legacy endpoints for backward compatibility
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestParam Long userId) {
        orderService.placeOrder(userId);
        return ResponseEntity.ok("Order placed successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/orders")
    public List<OrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

}
