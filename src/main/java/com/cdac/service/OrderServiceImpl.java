package com.cdac.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.CartDao;
import com.cdac.dao.OrderDao;
import com.cdac.dto.OrderDTO;
import com.cdac.entities.Cart;
import com.cdac.entities.Order;
import com.cdac.entities.OrderStatus;
import com.cdac.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final CartDao cartDao;
    private final ModelMapper modelMapper;

    @Override
    public OrderDTO placeOrder(Long userId) {
        // 1. Load cart for user
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getBooks() == null || cart.getBooks().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty for user: " + userId);
        }

        // 2. Build Order entity
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        // copy books from cart (shallow copy of references)
        order.setBooks(List.copyOf(cart.getBooks()));
        // calculate total
        double total = cart.getBooks().stream()
                .mapToDouble(b -> b.getPrice() != null ? b.getPrice() : 0.0)
                .sum();
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        // set user who placed order (cart should have user)
        order.setUser(cart.getUser());

        // 3. Save order
        Order saved = orderDao.save(order);

        // 4. Clear cart after successful order placement
        cart.getBooks().clear();
        cart.setTotalPrice(0.0);
        cartDao.save(cart);

        // 5. Map to DTO and return
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderDao.findByUserId(userId).stream()
            .map(order -> modelMapper.map(order, OrderDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return modelMapper.map(order, OrderDTO.class);
    }

}
