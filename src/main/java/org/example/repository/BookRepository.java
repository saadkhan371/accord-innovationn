package org.example.repository;

import org.example.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("select b from Book b join b.author a where a.name = :authorName")
    Page<Book> findByAuthorName(@Param("authorName") String authorName, Pageable pageable);
}
