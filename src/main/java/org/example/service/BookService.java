package org.example.service;

import org.example.dto.AuthorDto;
import org.example.dto.BookDto;
import org.example.model.Author;
import org.example.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.restTemplate = restTemplate;
    }

    public Book save(Book book) {
        if (book.getAuthor() != null && book.getAuthor().getId() == null) {
            Author saved = authorRepository.save(book.getAuthor());
            book.setAuthor(saved);
        }
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) { return bookRepository.findById(id); }

    public void delete(Long id) { bookRepository.deleteById(id); }

    public Page<Book> searchByTitle(String title, int page, int size) {
        return bookRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public Page<Book> searchByAuthor(String authorName, int page, int size) {
        return bookRepository.findByAuthorName(authorName, PageRequest.of(page, size));
    }

    // Example external API call
    public String callExternalGoogle() {
        return restTemplate.getForObject("https://www.google.com", String.class);
    }

    // Mapping helpers between entity and record DTOs
    public BookDto toDto(Book b) {
        if (b == null) return null;
        return new BookDto(b.getId(), b.getTitle(), b.getIsbn(), toDto(b.getAuthor()));
    }

    public AuthorDto toDto(Author a) {
        if (a == null) return null;
        return new AuthorDto(a.getId(), a.getName());
    }

    public Book toEntity(BookDto d) {
        if (d == null) return null;
        Author author = null;
        if (d.author() != null) {
            Long aid = d.author().id();
            if (aid != null) {
                author = authorRepository.findById(aid).orElseGet(() -> new Author(d.author().name()));
            } else {
                author = new Author(d.author().name());
            }
        }
        Book book = new Book(d.title(), d.isbn(), author);
        if (d.id() != null) book.setId(d.id());
        return book;
    }
}
