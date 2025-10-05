package com.cdac.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.OrderDao;
import com.cdac.dao.TransactionDao;
import com.cdac.dao.UserDao;
import com.cdac.dto.TransactionDTO;
import com.cdac.entities.Order;
import com.cdac.entities.Transaction;
import com.cdac.entities.TransactionStatus;
import com.cdac.entities.User;
import com.cdac.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        // Validate user if provided (otherwise you can make userId required on DTO)
        if (transactionDTO.getOrderId() == null) {
            throw new IllegalArgumentException("orderId must be provided");
        }

        // Load order (and implicitly user through order)
        Order order = orderDao.findById(transactionDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + transactionDTO.getOrderId()));

        // Optional: If you want userId in DTO to be authoritative, validate it
        if (transactionDTO.getOrderId() != null && transactionDTO.getOrderId() > 0 && transactionDTO.getOrderId().equals(order.getId())) {
            // OK
        }

        // If DTO contains a userId, verify user exists (alternatively derive user from order)
        User user = null;
        if (transactionDTO.getUserId() != null) {
            user = userDao.findById(transactionDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + transactionDTO.getUserId()));
            // Optional: check user matches order.getUser()
            if (order.getUser() != null && !order.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Provided userId does not match order's user");
            }
        } else {
            // derive user from order (common approach)
            user = order.getUser();
        }

        // Map DTO -> entity, then set associations and timestamp
        Transaction tx = modelMapper.map(transactionDTO, Transaction.class);
        tx.setOrder(order);
        tx.setAmount(transactionDTO.getAmount());
        tx.setPaymentId(transactionDTO.getPaymentId());
        tx.setPaymentMethod(transactionDTO.getPaymentMethod());
        tx.setStatus(TransactionStatus.PENDING);
        tx.setTransactionDate(LocalDateTime.now());
        tx.setOrder(order);
        tx.setPaymentId(transactionDTO.getPaymentId());
        // set user if your Transaction entity has a user field (if not, skip)
        // tx.setUser(user); // uncomment if Transaction has User mapping

        Transaction saved = transactionDao.save(tx);
        log.info("Created transaction id={} for order={}", saved.getId(), order.getId());
        return modelMapper.map(saved, TransactionDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        // validate user exists
        if (!userDao.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // use DAO method that finds transactions by order->user relationship
        List<Transaction> transactions = transactionDao.findByOrderUserId(userId);

        return transactions.stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());
    }
}
