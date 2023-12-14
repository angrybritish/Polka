package com.example.Polka.repository;

import com.example.Polka.database.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>  {
   Optional<Comment> findById(Long id);
   Optional<Comment> findByIdOwnAndIdBook(Long id,Long idBook);
   List<Comment> findByIdBook(Long id);
}