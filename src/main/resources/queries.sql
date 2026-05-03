-- ============================================================
-- QUERY 1: Login — SELECT user by username
-- URL: POST /login (Spring Security)
-- Type: Retrieval
-- ============================================================
SELECT * FROM users WHERE username = ?;


-- ============================================================
-- QUERY 2: Register — INSERT new user
-- URL: POST /createAccount
-- Type: Insertion
-- ============================================================
INSERT INTO users (username, password_hash) VALUES (?, ?);


-- ============================================================
-- QUERY 3: Genre list — SELECT all genres
-- URL: GET /genres
-- Type: Retrieval
-- ============================================================
SELECT genreID, name, description FROM Genre;


-- ============================================================
-- QUERY 4: Movies by genre — SELECT with JOIN
-- URL: GET /genres/{id}
-- Type: Retrieval | JOIN
-- ============================================================
SELECT m.movieID, m.title, m.releaseDate
FROM Movie m
JOIN MovieGenre mg ON m.movieID = mg.movieID
WHERE mg.genreID = ?;


-- ============================================================
-- QUERY 5: Movie detail page — SELECT single movie
-- URL: GET /movies/{id}
-- Type: Retrieval
-- ============================================================
SELECT movieID, title, releaseDate, tagline FROM Movie WHERE movieID = ?;


-- ============================================================
-- QUERY 6: Reviews for a movie — SELECT with JOIN + AVG aggregate
-- URL: GET /movies/{id}
-- Type: Retrieval | JOIN | Aggregation
-- ============================================================
SELECT u.username, r.rating, r.comment, r.date
FROM Review r
JOIN users u ON r.userID = u.userID
WHERE r.movieID = ?;

-- Average rating for display:
SELECT ROUND(AVG(rating), 1) AS avgRating FROM Review WHERE movieID = ?;


-- ============================================================
-- QUERY 7: Submit review — INSERT new review
-- URL: POST /movies/{id}/review
-- Type: Insertion
-- ============================================================
INSERT INTO Review (rating, comment, date, userID, movieID) VALUES (?, ?, CURDATE(), ?, ?);


-- ============================================================
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
-- Note: Replace [orderBy] with the appropriate ORDER BY clause based on the 'sort' parameter:
-- For sort=rating: ORDER BY avgRating DESC, reviewCount DESC
-- For sort=reviews: ORDER BY reviewCount DESC, avgRating DESC

