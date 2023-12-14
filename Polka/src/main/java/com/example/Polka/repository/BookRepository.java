package com.example.Polka.repository;

import com.example.Polka.database.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
public interface BookRepository extends JpaRepository<Book, Long>  {
    Optional<Book> findById(Long id);
    List<Book> findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenre(String name,String author, int genre);
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM bookspolka ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Book> findRandomBooks();

    List<Book> findAllByStatusContainingIgnoreCase(String status);

    List<Book> findAllByIdOwn(Long id);
}