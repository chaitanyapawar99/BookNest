package com.cdac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.TransactionDTO;
import com.cdac.service.TransactionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor

public class TransactionController 
{
	 private final TransactionService transactionService;

	    @PostMapping("/create")
	    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
	        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
	    }

//	    @GetMapping("/{userId}")
//	    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@PathVariable Long userId) {
//	        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
//	    }
}
