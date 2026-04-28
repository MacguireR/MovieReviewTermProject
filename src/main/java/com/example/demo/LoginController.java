package com.example.demo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class LoginController {
    @Autowired
    private DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
        @RequestParam String username,
        @RequestParam String password,
        Model model) {
            String sql =
            "SELECT password_hash FROM users WHERE username = ?";
            try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if(passwordEncoder.matches(password, storedHash)){
                        return "redirect:/home";
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("error", true);
            return "login";
        }
}