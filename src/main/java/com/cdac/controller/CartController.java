package com.cdac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.AddToCartRequest;
import com.cdac.service.CartService;
import com.cdac.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController 
{
	private final CartService cartService;
	private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getCurrentUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/books")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        cartService.addToCart(userId, request.getBookId());
        return ResponseEntity.ok("Book added to cart");
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        cartService.removeFromCart(userId, bookId);
        return ResponseEntity.ok("Book removed from cart");
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam Long userId, @RequestParam Long bookId) {
        cartService.removeFromCart(userId, bookId);
        return ResponseEntity.ok("Book removed from cart");
    }
}
