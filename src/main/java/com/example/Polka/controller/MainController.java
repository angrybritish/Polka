package com.example.Polka.controller;

import com.example.Polka.database.File;
import com.example.Polka.database.User;
import com.example.Polka.functions.func;
import com.example.Polka.managers.BookManager;
import com.example.Polka.managers.FileManager;
import com.example.Polka.managers.SavedManager;
import com.example.Polka.managers.UserManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.Polka.functions.func.*;

@Controller
public class MainController {
    private static final int PAGE_SIZE = 5000; // Размер страницы в символах
    private static final String BOOKS_DIRECTORY = "src/main/uploads"; // Путь к папке
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
            model.addAttribute("body", getBook(BookManager.getBookById(id).get(),true,userManager.getUserByMail((String) session.getAttribute("user")).get()));
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            return "wauto";
        }
        else {
            User user = new User();
            model.addAttribute("body", getBook(BookManager.getBookById(id).get(),false,user));
            return "nonauto";
        }
    }
    @GetMapping("/my/{saved}")
    public String lkMy(HttpSession session, Model model,@PathVariable String saved) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(), saved));
            return "wauto";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/booklist/{idbooklist}")
    public String BookList(HttpSession session, Model model,@PathVariable Long idbooklist) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkBooklist(idbooklist));
            return "wauto";
        } else {
            model.addAttribute("body", getLkBooklist(idbooklist));
            return "nonauto";
        }
    }

    @GetMapping("/lk/support")
    public String lkSupport(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkSupport(userManager.getUserByMail((String) session.getAttribute("user")).get()));
            return "wauto";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/buybook/{id}")
    public String buyBook(HttpSession session, Model model,@PathVariable Long id) {
        if (session.getAttribute("user") != null && !SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),BookManager.getBookById(id).get().getId(),"buy",false).isPresent()) {
            session.setAttribute("book", id);
            model.addAttribute("buyInfo", "К оплате " + BookManager.getBookById(id).get().getPrice());
            return "buy";
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/readbook/{id}&{page}", produces = "text/html; charset=UTF-8")
    public String readBook(HttpSession session, Model model,@PathVariable Long id,@PathVariable int page) {
        if (session.getAttribute("user") != null && SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),BookManager.getBookById(id).get().getId(),"buy",false).isPresent()) {
            File file = FileManager.getFileByIdBook(id,"txt").get();
            // Указываем путь к файлу
            Path filePath = Paths.get(BOOKS_DIRECTORY, file.getGenName() + "." + file.getType());

            // Вычисляем начальный индекс символов для чтения из файла
            int startCharIndex = (page - 1) * PAGE_SIZE;

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile(), StandardCharsets.UTF_8))) {
                // Перемещаемся к нужному смещению в файле
                reader.skip(startCharIndex);

                // Считываем данные из файла в буфер
                char[] buffer = new char[PAGE_SIZE];
                int charsRead = reader.read(buffer, 0, PAGE_SIZE);

                // Если файл был прочитан до конца, возвращаем соответствующее сообщение
                if (charsRead == -1) {
                    model.addAttribute("body", "Достигнут конец файла или страницы нет.");
                    return "wauto";
                }

                // Возвращаем результат чтения в формате String
                int OldPage = page - 1;
                int FutPage = page + 1;
                String buttons = "<br><button type=\"submit\" onclick=\"window.location.href='../readbook/"+id+"&"+OldPage+"';\" class=\"btn btn-warning\">Назад</button>&nbsp;<button type=\"submit\" onclick=\"window.location.href='../readbook/"+id+"&"+FutPage+"';\" class=\"btn btn-warning\">Дальше</button>&nbsp;";
                model.addAttribute("body", new String(buffer, 0, charsRead) + buttons);
                return "wauto";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("body", "Произошла ошибка при чтении файла.");
                return "wauto";
            }
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping("/admin/books")
    public String buyBook(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 2) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkAdminBooks());
            return "wauto";
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping("/admin/support")
    public String supportAdmin(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 2) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkAdminSupport());
            return "wauto";
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping("/lk/books")
    public String BooksWritter(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 1) {
            model.addAttribute("menu", getLkMenu(userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType()));
            model.addAttribute("body", getLkBooksWritter(userManager.getUserByMail((String) session.getAttribute("user")).get().getId()));
            return "wauto";
        }
        else {
            return "redirect:/login";
        }
    }

}