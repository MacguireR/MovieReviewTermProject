package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final JdbcTemplate jdbcTemplate;
    private static final int PAGE_SIZE = 25;

    public HomeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping({"/", "/home"})
    public String homePage(HttpSession session,
                           Model model,
                           @RequestParam(defaultValue = "0") int page) {

        int offset = page * PAGE_SIZE;

        String sql = """
            SELECT 
                m.movieID,
                m.title,
                m.releaseDate,
                m.tagline,
                GROUP_CONCAT(g.name SEPARATOR ', ') AS genres,
                ROUND(AVG(r.rating), 1) AS avgRating
            FROM Movie m
            LEFT JOIN MovieGenre mg ON m.movieID = mg.movieID
            LEFT JOIN Genre g ON mg.genreID = g.genreID
            LEFT JOIN Review r ON m.movieID = r.movieID
            GROUP BY m.movieID, m.title, m.releaseDate, m.tagline
            ORDER BY m.movieID
            LIMIT ? OFFSET ?
        """;

        String countSql = "SELECT COUNT(*) FROM Movie";

        List<Map<String, Object>> movies = jdbcTemplate.queryForList(sql, PAGE_SIZE, offset);
        int totalMovies = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPages = (int) Math.ceil((double) totalMovies / PAGE_SIZE);

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("movies", movies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < totalPages - 1);

        return "home";
    }
}