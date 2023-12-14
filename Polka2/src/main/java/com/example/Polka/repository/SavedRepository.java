package com.example.Polka.repository;

import com.example.Polka.database.Saved;
import com.example.Polka.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedRepository extends JpaRepository<Saved, Long> {
    Optional<Saved> findByIdOwnAndIdBookAndStatusAndDeleted(Long idOwn,Long idBook,String status,Boolean deleted);
}
