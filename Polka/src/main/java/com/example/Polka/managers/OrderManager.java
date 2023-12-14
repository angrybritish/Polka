package com.example.Polka.managers;

import com.example.Polka.database.File;
import com.example.Polka.database.Order;
import com.example.Polka.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderManager {
    @Autowired
    private static OrderRepository orderRepository;

    public OrderManager(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public static Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public static Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
