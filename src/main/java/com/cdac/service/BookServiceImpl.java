package com.cdac.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.BookDao;
import com.cdac.dao.CategoryDao;
import com.cdac.dto.BookDTO;
import com.cdac.entities.Book;
import com.cdac.entities.Category;
import com.cdac.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookDao.findAll().stream()
            .map(book -> modelMapper.map(book, BookDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);

        // Set category if provided
        if (bookDTO.getCategoryId() != null) {
            Category category = categoryDao.findById(bookDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDTO.getCategoryId()));
            book.setCategory(category);
        }

        // Optionally set the seller from security context (example)
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
        //     User seller = (User) auth.getPrincipal();
        //     book.setSeller(seller);
        // }

        Book saved = bookDao.save(book);
        return modelMapper.map(saved, BookDTO.class);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        return bookDao.findByCategoryId(categoryId).stream()
            .map(book -> modelMapper.map(book, BookDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Update simple fields
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setImagePath(bookDTO.getImagePath());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setAvailable(bookDTO.isAvailable());
        existingBook.setApproved(bookDTO.isApproved());

        // Update category if provided
        if (bookDTO.getCategoryId() != null) {
            Category category = categoryDao.findById(bookDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDTO.getCategoryId()));
            existingBook.setCategory(category);
        }

        Book saved = bookDao.save(existingBook);
        return modelMapper.map(saved, BookDTO.class);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookDao.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookDao.deleteById(id);
    }
}
