package com.cdac.service;

import java.util.List;

import com.cdac.dto.OrderDTO;

public interface OrderService 
{
	OrderDTO placeOrder(Long userId);
    List<OrderDTO> getOrdersByUserId(Long userId);
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long orderId);
}
