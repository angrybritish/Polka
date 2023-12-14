package com.example.Polka.repository;

import com.example.Polka.database.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long>  {
    Optional<File> findAllById(Long id);

    Optional<File> findById(Long id);
    Optional<File> findAllByIdBookAndType(Long id,String type);
}
