package Controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String login(@NotNull Model model) {
        model.addAttribute("title");
        return "login";
    }
}
