package com.example.Polka.repository;

import com.example.Polka.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByMail(String mail);

    Optional<User> findById(Long id);

    List<User> findAll();
}
