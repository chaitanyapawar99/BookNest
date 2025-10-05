package com.cdac.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Transaction;
import com.cdac.entities.TransactionStatus;

public interface TransactionDao extends JpaRepository<Transaction, Long>
{
	Optional<Transaction> findByPaymentId(String paymentId);

    List<Transaction> findByOrderUserId(Long userId);

    // Find all transactions within a date range
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    // Find by status (e.g., SUCCESS, FAILED)
    List<Transaction> findByStatus(TransactionStatus status);

    // Find all transactions for a specific user and status
    List<Transaction> findByOrderUserIdAndStatus(Long userId, TransactionStatus status);
}
