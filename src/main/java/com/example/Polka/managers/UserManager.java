package com.example.Polka.managers;

import com.example.Polka.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Polka.repository.UserRepository;

import java.util.List;
import java.util.Optional;
@Service
public class UserManager {
    @Autowired
    private static UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static User createUser(User user) {
        return userRepository.save(user);
    }

    public static Optional<User> getUserByMail(String mail) {
        return userRepository.findByMail(mail);
    }
    public static Optional<User> getUserById(Long Id) {
        return userRepository.findById(Id);
    }

    public static List<User> findAllUsers() {
        return userRepository.findAll();
    }
}