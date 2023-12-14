package com.example.Polka.managers;

import com.example.Polka.database.Comment;
import com.example.Polka.database.Compilation;
import com.example.Polka.repository.CompilationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompilationManager {
    @Autowired
    private static CompilationRepository compilationRepository;

    public CompilationManager(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }
    public static Compilation createCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    public static List<Compilation> getCompByIdOwn(Long id) {
        return compilationRepository.findByIdOwn(id);
    }

    public static Optional<Compilation> getCompById(Long id) {
        return compilationRepository.findById(id);
    }
}
