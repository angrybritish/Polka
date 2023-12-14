package com.example.Polka.controller;

import com.example.Polka.managers.BookManager;
import com.example.Polka.managers.UserManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.Polka.functions.func.*;

@Controller
public class MainController {
    UserManager userManager;
    @GetMapping("/")
    public String main(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/lk";
        } else {
            model.addAttribute("body", getLkRecomendations());
            return "nonauto";
        }
    }
    @GetMapping("/lk")
    public String lk(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkRecomendations());
            return "wauto";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/lk/publish")
    public String lkPublish(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 1) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkPublish(userManager.getUserByMail((String) session.getAttribute("user")).get().getId()));
            return "wauto";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/search")
    public String lkSearch(HttpSession session, Model model,@RequestParam(value = "query") String query,@RequestParam(value = "option") int option) {
        model.addAttribute("body", getSearch(query,option));
        if (session.getAttribute("user") != null) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            return "wauto";
        }
        else {
            return "nonauto";
        }
    }
    @GetMapping("/book/{id}")
    public String lkSearch(HttpSession session, Model model,@PathVariable Long id) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("body", getBook(BookManager.getBookById(id).get(),true));
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            return "wauto";
        }
        else {
            model.addAttribute("body", getBook(BookManager.getBookById(id).get(),false));
            return "nonauto";
        }
    }
    @GetMapping("/lk/my")
    public String lkMy(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 1) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkPublish(userManager.getUserByMail((String) session.getAttribute("user")).get().getId()));
            return "wauto";
        } else {
            return "redirect:/login";
        }
    }
}