package org.example;

import org.example.model.Author;
import org.example.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public DataLoader(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Author a1 = new Author("John Elia");
        Author a2 = new Author("Faiz Ahmed Faiz");
        authorRepository.save(a1);
        authorRepository.save(a2);

        bookRepository.save(new Book("Shayad", "ISBN-001", a1));
        bookRepository.save(new Book("Poems By Faiz", "ISBN-002", a2));
        bookRepository.save(new Book("Kulliyat-e-John", "ISBN-003", a1));
    }
}
