-- Indexes for Insights page performance
-- Query pattern optimized:
--   LEFT JOIN Review r ON m.movieID = r.movieID
--   GROUP BY m.movieID
--   AVG(r.rating), COUNT(r.reviewID)

CREATE INDEX idx_review_movieid ON Review(movieID);

CREATE INDEX idx_review_movieid_rating ON Review(movieID, rating);

