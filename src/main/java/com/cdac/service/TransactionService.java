package com.cdac.service;

import java.util.List;

import com.cdac.dto.TransactionDTO;

public interface TransactionService 
{
	TransactionDTO createTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> getTransactionsByUserId(Long userId);
//    List<TransactionDTO> getTransactionsByUser(Long userId);
}
