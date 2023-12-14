package com.example.Polka.repository;

import com.example.Polka.database.File;
import com.example.Polka.database.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);
}
