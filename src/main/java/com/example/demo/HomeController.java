package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        return "home";
    }
}
