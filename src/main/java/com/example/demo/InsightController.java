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
public class InsightController {
    
    private final JdbcTemplate jdbcTemplate;
    private static final int PAGE_SIZE = 100;

    public InsightController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/insights")
    public String insightsPage(HttpSession session, Model model, @RequestParam(defaultValue = "rating") String sort) {
        String orderBy;
        if ("reviews".equalsIgnoreCase(sort)) {
            orderBy = "reviewCount DESC, avgRating DESC, m.title ASC";
            sort = "reviews";
        } else {
            orderBy = "avgRating DESC, reviewCount DESC, m.title ASC";
        }

        String sql = "SELECT m.movieID, m.title, ROUND(AVG(r.rating), 1) AS avgRating, " +
                "COUNT(r.reviewID) AS reviewCount FROM Movie m " +
                "LEFT JOIN Review r ON m.movieID = r.movieID " +
                "GROUP BY m.movieID, m.title ORDER BY " + orderBy + " LIMIT " + PAGE_SIZE;

        List<Map<String, Object>> movies = jdbcTemplate.queryForList(sql);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("movies", movies);
        model.addAttribute("sort", sort);

        return "insights";
    }

}
