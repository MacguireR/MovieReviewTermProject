-- spring.sql.init.mode=always
-- put this is application.properties to make this auto-run
-- spring.jpa.hibernate.ddl-auto=update needs to be changed to = none though
-- using IF NOT EXISTS in case we use the above mention auto-run

CREATE TABLE IF NOT EXISTS Genre (
    genreID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Movie (
    movieID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    releaseDate DATE,
    director VARCHAR(255),
    rating DECIMAL(3,1),
    genreID INT,
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
    movieID  INT NOT NULL,
    FOREIGN KEY (userID)  REFERENCES User(userID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID)
);
