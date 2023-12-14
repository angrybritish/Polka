package com.example.Polka.managers;

import com.example.Polka.database.Comment;
import com.example.Polka.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CommentManager {
    @Autowired
    private static CommentRepository commentRepository;

    public CommentManager(CommentRepository bookRepository) {
        this.commentRepository = bookRepository;
    }
    public static Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public static Optional<Comment> getUserCommById(Long id, Long idBook) {
        return commentRepository.findByIdOwnAndIdBook(id,idBook);
    }

    public static Optional<Comment> getUserCommByIdComm(Long id) {
        return commentRepository.findById(id);
    }
    public static List<Comment> getCommByIdBook(Long id) {
        return commentRepository.findByIdBook(id);
    }
}
