package com.cdac.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Book;

public interface BookDao extends JpaRepository<Book, Long> 
{
	// Search books by title
    List<Book> findByTitleContainingIgnoreCase(String title);

    // All books in a category
    List<Book> findByCategoryId(Long categoryId);

    // Books by seller
    List<Book> findBySellerId(Long sellerId);

    // Approved books only
    List<Book> findByApprovedTrue();

    // Approved + available books
    List<Book> findByApprovedTrueAndAvailableTrue();

    // Approved books in category
    List<Book> findByCategoryIdAndApprovedTrue(Long categoryId);

    // Available books for a specific seller
    List<Book> findBySellerIdAndAvailableTrue(Long sellerId);

}
