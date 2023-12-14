package com.example.Polka.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegController {
    @GetMapping("/register")
    public String reg(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "reg";
        }
        else {
            return "redirect:/lk";
        }
    }
}
