package com.example.Polka.repository;

import com.example.Polka.database.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findByIdOwn(Long id);

    Optional<Compilation> findById(Long id);
}
