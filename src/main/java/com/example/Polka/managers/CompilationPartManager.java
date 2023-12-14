package com.example.Polka.managers;

import com.example.Polka.database.CompilationPart;
import com.example.Polka.repository.CompilationPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompilationPartManager {
    @Autowired
    private static CompilationPartRepository compilationPartRepository;

    public CompilationPartManager(CompilationPartRepository compilationPartRepository) {
        this.compilationPartRepository = compilationPartRepository;
    }
    public static CompilationPart createCompilationPart(CompilationPart compilationPart) {
        return compilationPartRepository.save(compilationPart);
    }

    public static Optional<CompilationPart> getPart(Long idBook, Long idCompilation, Boolean deleted) {
        return compilationPartRepository.findByIdbookAndIdCompilationAndDeleted(idBook,idCompilation, deleted);
    }

    public static List<CompilationPart> getByIdComp(Long id) {
        return compilationPartRepository.findByIdCompilation(id);
    }
}
