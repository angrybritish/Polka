package com.example.Polka.repository;

import com.example.Polka.database.CompilationPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompilationPartRepository extends JpaRepository<CompilationPart, Long> {
    Optional<CompilationPart> findByIdbookAndIdCompilationAndDeleted(Long idBook, Long idCompilation, Boolean deleted);

    List<CompilationPart> findByIdCompilation(Long idCompilation);
}
