package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class MovieController {

    private final JdbcTemplate jdbcTemplate;

    public MovieController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/movie")
    public String movieDetail(@RequestParam int id,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        String movieSql = """
            SELECT 
                m.movieID,
                m.title,
                m.releaseDate,
                m.tagline,
                GROUP_CONCAT(DISTINCT g.name SEPARATOR ', ') AS genres,
                (
                    SELECT ROUND(AVG(r2.rating), 1)
                    FROM Review r2
                    WHERE r2.movieID = m.movieID
                ) AS avgRating,
                (
                    SELECT COUNT(*)
                    FROM Review r3
                    WHERE r3.movieID = m.movieID
                ) AS reviewCount
            FROM Movie m
            LEFT JOIN MovieGenre mg ON m.movieID = mg.movieID
            LEFT JOIN Genre g ON mg.genreID = g.genreID
            WHERE m.movieID = ?
            GROUP BY m.movieID, m.title, m.releaseDate, m.tagline
        """;

        List<Map<String, Object>> movieResult = jdbcTemplate.queryForList(movieSql, id);

        if (movieResult.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Movie not found.");
            return "redirect:/home";
        }

        String reviewSql = """
            SELECT 
                r.reviewID,
                r.rating,
                r.comment,
                r.date,
                u.username
            FROM Review r
            JOIN User u ON r.userID = u.userID
            WHERE r.movieID = ?
            ORDER BY r.date DESC
        """;

        List<Map<String, Object>> reviews = jdbcTemplate.queryForList(reviewSql, id);

        String username = (String) session.getAttribute("username");
        boolean alreadyReviewed = false;

        if (username != null) {
            String checkSql = """
                SELECT COUNT(*)
                FROM Review r
                JOIN User u ON r.userID = u.userID
                WHERE r.movieID = ? AND u.username = ?
            """;

            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id, username);
            alreadyReviewed = count != null && count > 0;
        }

        model.addAttribute("movie", movieResult.get(0));
        model.addAttribute("reviews", reviews);
        model.addAttribute("username", username);
        model.addAttribute("alreadyReviewed", alreadyReviewed);

        return "movie";
    }

    @PostMapping("/movie/{id}/review")
    public String submitReview(@PathVariable int id,
                               @RequestParam int rating,
                               @RequestParam(required = false) String comment,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to leave a review.");
            return "redirect:/movie?id=" + id;
        }

        if (rating < 1 || rating > 10) {
            redirectAttributes.addFlashAttribute("error", "Rating must be between 1 and 10.");
            return "redirect:/movie?id=" + id;
        }

        String userIdSql = "SELECT userID FROM User WHERE username = ?";
        List<Map<String, Object>> userResult = jdbcTemplate.queryForList(userIdSql, username);

        if (userResult.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/movie?id=" + id;
        }

        int userId = ((Number) userResult.get(0).get("userID")).intValue();

        String alreadyReviewedSql = """
            SELECT COUNT(*)
            FROM Review
            WHERE movieID = ? AND userID = ?
        """;

        Integer reviewCount = jdbcTemplate.queryForObject(alreadyReviewedSql, Integer.class, id, userId);

        if (reviewCount != null && reviewCount > 0) {
            redirectAttributes.addFlashAttribute("error", "You already reviewed this movie.");
            return "redirect:/movie?id=" + id;
        }

        String insertSql = """
            INSERT INTO Review (rating, comment, date, userID, movieID)
            VALUES (?, ?, CURDATE(), ?, ?)
        """;

        try {
            jdbcTemplate.update(insertSql, rating, comment, userId, id);
            redirectAttributes.addFlashAttribute("success", "Review submitted!");
        } catch (DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("error", "Duplicate review.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/movie?id=" + id;
    }
}