package com.cdac.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.BookDTO;
import com.cdac.service.BookService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
@Slf4j
@Validated
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        log.debug("Adding book: {}", bookDTO.getTitle());
        BookDTO created = bookService.addBook(bookDTO);
        return ResponseEntity.status(201).body(created); // 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Recommended: use pagination for lists.
     * If service returns Page<BookDTO>, controller can directly return that.
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(Pageable pageable) {
        // If your BookService currently returns List, you can either keep it
        // or switch service to return Page<BookDTO> for production scalability.
        List<BookDTO> books = bookService.getAllBooks(); 
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updated = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
