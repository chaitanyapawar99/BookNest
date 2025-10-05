package com.cdac.service;

import java.util.List;

import com.cdac.dto.BookDTO;

public interface BookService 
{
	List<BookDTO> getAllBooks();
    BookDTO addBook(BookDTO bookDTO);
    BookDTO getBookById(Long id);
    List<BookDTO> getBooksByCategory(Long categoryId);
    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBook(Long id);
}
