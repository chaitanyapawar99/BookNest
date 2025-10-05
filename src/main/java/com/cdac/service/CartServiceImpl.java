package com.cdac.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.custom_exceptions.ResourceNotFoundException;
import com.cdac.dao.BookDao;
import com.cdac.dao.CartDao;
import com.cdac.dao.UserDao;
import com.cdac.dto.CartDTO;
import com.cdac.entities.Book;
import com.cdac.entities.Cart;
import com.cdac.entities.User;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;
    private final BookDao bookDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartDao.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartDTO addToCart(Long userId, Long bookId) {
        User user = userDao.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Book book = bookDao.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
        Cart cart = cartDao.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalPrice(0.0);
            return newCart;
        });
        // Prevent duplicate entries (one book only once per cart)
        if (cart.getBooks().stream().noneMatch(b -> b.getId().equals(bookId))) {
            cart.getBooks().add(book);
        }
        cart.setTotalPrice(calculateTotal(cart));
        Cart saved = cartDao.save(cart);
        return modelMapper.map(saved, CartDTO.class);
    }

    private Double calculateTotal(Cart cart) {
        return cart.getBooks().stream()
            .mapToDouble(b -> b.getPrice() != null ? b.getPrice() : 0.0)
            .sum();
    }

    @Override
    public void removeFromCart(Long userId, Long bookId) {
        Cart cart = cartDao.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));
        boolean removed = cart.getBooks().removeIf(b -> b.getId().equals(bookId));
        if (removed) {
            cart.setTotalPrice(calculateTotal(cart));
            cartDao.save(cart);
        } else {
            throw new ResourceNotFoundException("Book not found in cart: " + bookId);
        }
    }
}
