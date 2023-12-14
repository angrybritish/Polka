package com.example.Polka.managers;

import com.example.Polka.database.Book;
import com.example.Polka.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BookManager {
    @Autowired
    private static BookRepository bookRepository;

    public BookManager(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public static Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public static Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public static List<Book> readAllByNA(String query) {
        return bookRepository.findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(query);
    }
    public static List<Book> readAllByIdOwn(Long id) {
        return bookRepository.findAllByIdOwn(id);
    }
    public static List<Book> readAllByNAG(String name,String author,int genre) {
        return bookRepository.findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenre(name,author,genre);
    }
    public static List<Book> readAllByRandom10() {
        return bookRepository.findRandomBooks();
    }
    public static List<Book> readAllByStatus(String status) {
        return bookRepository.findAllByStatusContainingIgnoreCase(status);
    }
}