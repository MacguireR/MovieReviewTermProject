-- spring.sql.init.mode=always
-- put this in application.properties to make this auto-run
-- spring.jpa.hibernate.ddl-auto=update needs to be changed to = none though
-- using IF NOT EXISTS in case we use the above mentioned auto-run

CREATE TABLE IF NOT EXISTS Genre (
    genreID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Movie (
    movieID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    releaseDate DATE,
    tagline TEXT
);

-- SELECT AVG(rating) FROM Review WHERE movieID = ?
-- this is for getting ratings

CREATE TABLE IF NOT EXISTS MovieGenre (
    movieID INT NOT NULL,
    genreID INT NOT NULL,
    PRIMARY KEY (movieID, genreID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID),
    FOREIGN KEY (genreID) REFERENCES Genre(genreID)
);

CREATE TABLE IF NOT EXISTS User (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Review (
    reviewID INT PRIMARY KEY AUTO_INCREMENT,
    rating INT NOT NULL,
    comment TEXT,
    date DATE NOT NULL,
    userID INT NOT NULL,
    movieID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID)
);
