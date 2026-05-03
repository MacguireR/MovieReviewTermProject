package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
public class accountController {
    @Autowired
    private DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @GetMapping("/createAccount")
    public String accountPage() {
        return "createAccount";
    }
    @PostMapping("/createAccount")
    public String createAccount(@RequestParam String username, @RequestParam String password) {
        String hashed = passwordEncoder.encode(password);
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO User (username, password_hash) VALUES (?, ?)"
            );
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/createAccount?error";
        }
        return "redirect:/login?created";
    }
}
