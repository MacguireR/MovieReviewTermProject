-- INSIGHTS PAGE QUERY
-- URL: GET /insights
-- Purpose: Display top 100 movies sorted by either rating or review count
-- Used in: InsightController.insightsPage()
-- Supports two sorting modes via @RequestParam sort:
--   1. sort=rating (default): Orders by AVG(rating) DESC, then reviewCount DESC
--   2. sort=reviews: Orders by reviewCount DESC, then AVG(rating) DESC

SELECT 
    m.movieID,
    m.title,
    ROUND(AVG(r.rating), 1) AS avgRating,
    COUNT(r.reviewID) AS reviewCount
FROM Movie m
LEFT JOIN Review r ON m.movieID = r.movieID
GROUP BY m.movieID, m.title
ORDER BY [orderBy] LIMIT [PAGE_SIZE];

