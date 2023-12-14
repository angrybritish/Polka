package com.example.Polka.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "login";
        }
        else {
            return "redirect:/lk";
        }
    }
}
