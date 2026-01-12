package org.example.controller;

import org.example.dto.BookDto;
import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) { this.bookService = bookService; }

    @GetMapping
    public Page<BookDto> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        return bookService.searchByTitle("", page, size).map(bookService::toDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> get(@PathVariable Long id) {
        return bookService.findById(id).map(bookService::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        Book toSave = bookService.toEntity(bookDto);
        Book saved = bookService.save(toSave);
        return ResponseEntity.ok(bookService.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return bookService.findById(id).map(existing -> {
            existing.setTitle(bookDto.title());
            existing.setIsbn(bookDto.isbn());
            existing.setAuthor(bookService.toEntity(new BookDto(null, null, null, bookDto.author())) != null ? bookService.toEntity(new BookDto(null, null, null, bookDto.author())).getAuthor() : existing.getAuthor());
            Book saved = bookService.save(existing);
            return ResponseEntity.ok(bookService.toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<BookDto> search(@RequestParam(required = false) String title,
                             @RequestParam(required = false) String author,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        if (author != null && !author.isBlank()) return bookService.searchByAuthor(author, page, size).map(bookService::toDto);
        if (title == null) title = "";
        return bookService.searchByTitle(title, page, size).map(bookService::toDto);
    }

    @GetMapping("/external/google")
    public ResponseEntity<String> google() {
        String body = bookService.callExternalGoogle();
        return ResponseEntity.ok(body != null ? "OK (fetched)" : "NO_CONTENT");
    }
}
