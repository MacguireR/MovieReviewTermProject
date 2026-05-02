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
public class GenreController {

    private final JdbcTemplate jdbcTemplate;

    public GenreController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/genres")
    public String listGenres(HttpSession session, Model model) {

        String sql = """
            SELECT genreID, name
            FROM Genre
            ORDER BY name
        """;

        List<Map<String, Object>> genres = jdbcTemplate.queryForList(sql);

        model.addAttribute("genres", genres);
        model.addAttribute("username", session.getAttribute("username"));

        return "genres";
    }

    @GetMapping("/genre")
    public String genreDetail(@RequestParam int id,
                              HttpSession session,
                              Model model) {

        // Get the genre name
        String genreSql = """
            SELECT genreID, name
            FROM Genre
            WHERE genreID = ?
        """;

        List<Map<String, Object>> genreResult = jdbcTemplate.queryForList(genreSql, id);

        if (genreResult.isEmpty()) {
            model.addAttribute("error", "Genre not found.");
            return "genres";
        }

        // Get movies that belong to this genre
        String movieSql = """
            SELECT 
                m.movieID, 
                m.title, 
                m.releaseDate, 
                m.tagline
            FROM Movie m
            JOIN MovieGenre mg ON m.movieID = mg.movieID
            WHERE mg.genreID = ?
            ORDER BY m.title
        """;

        List<Map<String, Object>> movies = jdbcTemplate.queryForList(movieSql, id);

        model.addAttribute("genre", genreResult.get(0));
        model.addAttribute("movies", movies);
        model.addAttribute("username", session.getAttribute("username"));

        return "genre";
    }
}