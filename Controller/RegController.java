package Controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegController {
    @GetMapping("/reg.html")
    public String reg(@NotNull Model model) {
        model.addAttribute("title");
        return "reg";
    }
}
