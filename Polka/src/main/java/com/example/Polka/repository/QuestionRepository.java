package com.example.Polka.repository;

import com.example.Polka.database.Comment;
import com.example.Polka.database.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByStatus(String status);

    Optional<Question> findById(Long id);
}
