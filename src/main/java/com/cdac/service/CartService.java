package com.cdac.service;

import com.cdac.dto.CartDTO;

public interface CartService 
{
	CartDTO getCartByUserId(Long userId);
    void removeFromCart(Long userId, Long bookId);
    CartDTO addToCart(Long userId, Long bookId);
}
