package com.example.Polka.managers;

import com.example.Polka.database.Comment;
import com.example.Polka.database.Order;
import com.example.Polka.database.Question;
import com.example.Polka.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionManager {
    @Autowired
    private static QuestionRepository questionRepository;

    public QuestionManager(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public static Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public static List<Question> getUserCommByIdComm(String status) {
        return questionRepository.findByStatus(status);
    }

    public static Optional<Question> getOrderById(Long id) {
        return questionRepository.findById(id);
    }
}
