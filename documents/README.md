# NAME'M MOVIES

Group: 12

---

## Team Members

- Evan
- Macguire
- Nate
- McKayla
- Adam

---

## Technologies Used

- Java 17
- Spring Boot 4.0.6
- Spring Security (BCrypt password hashing)
- Spring JDBC (PreparedStatement — no ORM)
- Thymeleaf (server-side templating)
- MySQL 8 (via Docker)
- Maven

**Dataset:** TMDB 5000 Movies (Kaggle) — see `datasource.txt`

---

## Database Connection

- **Database name:** demodb
- **Username:** appuser
- **Password:** apppass
- **JDBC URL:** `jdbc:mysql://127.0.0.1:33306/demodb`

---

## Test Accounts

| Username | Password |
|----------|----------|
| user1  | test  |
| user2  | pass  |
| user3  | mysql  |

---

## Database Schema

```sql
CREATE TABLE Genre (
    genreID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Movie (
    movieID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    releaseDate DATE,
    tagline TEXT
);

CREATE TABLE MovieGenre (
    movieID INT NOT NULL,
    genreID INT NOT NULL,
    PRIMARY KEY (movieID, genreID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID),
    FOREIGN KEY (genreID) REFERENCES Genre(genreID)
);

CREATE TABLE User (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE Review (
    reviewID INT PRIMARY KEY AUTO_INCREMENT,
    rating INT NOT NULL,
    comment TEXT,
    date DATE NOT NULL,
    userID INT NOT NULL,
    movieID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID)
);
```

- Movie rating is computed as `AVG(rating)` from the Review table — not stored directly.
- SELECT AVG(rating) FROM Review WHERE movieID = ?
- The above SQL query will get the average review for the desired movie.

---

## How to Run

1. Start the MySQL Docker container
2. Run the schema (first time only):
   ```bash
   mysql -h 127.0.0.1 -P 33306 -u appuser -p demodb < src/main/resources/ddl.sql
   ```
3. Load movie/genre data (first time only):
   ```bash
   mysql -h 127.0.0.1 -P 33306 -u appuser -p demodb < src/main/resources/data.sql
   ```
   Password: `apppass`
4. Apply performance indexes (first time only):
   ```bash
   cat src/main/resources/indexes.sql | docker exec -i moviereview-mysql mysql -uappuser -papppass demodb
   ```
5. Start the app: `./mvnw spring-boot:run`
6. Open `http://localhost:8080/login`

---

## Submission Checklist
X = done (definitely still double check)
o = in progress, needs to be finished
- [o] `prelim.pdf` — title, problem description, solution overview, ER diagram, technologies
- [o] `db_design.pdf` — ER model, ER-to-relation conversion, functional dependencies, normalization to BCNF/3NF
- [X] `ddl.sql` — all CREATE TABLE statements
- [X] `data.sql` — all INSERT statements to populate the demo database
- [o] `datasource.txt` — description and link to the TMDB dataset
- [o] `queries.sql` — all SQL queries used in the app with comments and URL paths
- [X] `perf.txt` — index creation statements, affected queries, timing before/after
- [o] `security.txt` — description of BCrypt password hashing approach
- [ ] Demo video
- [ ] Working code
- [o] `readme.txt` (this file, plain text copy for submission)
- [X] Group member contribution form (signed)

---

## Query Requirements Tracker

Minimum: 6 independent SQL statements (retrieval, insertion, deletion, update)
Minimum: 3 queries with aggregation or joins

| # | Query | Type | Has Join/Agg | Page |
|---|-------|------|--------------|------|
| 1 | SELECT user by username (login) | retrieval | | /login |
| 2 | INSERT new user (registration) | insertion | | /createAccount |
| 3 | SELECT all genres | retrieval | | /genres |
| 4 | SELECT movies by genre (JOIN MovieGenre) | retrieval | JOIN | /genres/{id} |
| 5 | SELECT reviews + username for a movie (JOIN users) | retrieval | JOIN | /movies/{id} |
| 6 | SELECT AVG(rating) for a movie | retrieval | AGG | /movies/{id} |
| 7 | INSERT new review | insertion | | /movies/{id} |
| 8 | SELECT top movies by AVG(rating) + COUNT(reviews) | retrieval | JOIN + AGG | /insights |

**Aggregation/Join queries:** #4 (JOIN), #5 (JOIN), #6 (AVG aggregate), #8 (LEFT JOIN + AVG + COUNT)

---

## TODO (in order)

### 1. Database
- [ ] Finalize and run `ddl.sql`
- [ ] Load TMDB movie data into Movie/Genre tables (`data.sql`)
- [ ] Ensure Movie table has 1000+ rows

### 2. Fix Auth
- [X] Handle duplicate username gracefully in `accountController`

### 3. Backend — Movies & Genres
- [ ] `GenreController`: GET `/genres`, GET `/genres/{id}`
- [ ] `MovieController`: GET `/movies`, GET `/movies/{id}`

### 4. Backend — Reviews
- [ ] `ReviewController`: POST `/movies/{id}/review` (pulls userID from session)
- [ ] Fetch reviews with username via JOIN on movie detail page

### 5. Frontend (5 distinct UIs required)
- [ ] Login / Create Account (exists)
- [ ] Home page (exists)
- [ ] Genre list page `/genres`
- [ ] Movie list page `/movies`
- [ ] Movie detail + reviews page `/movies/{id}`- [X] Insights page `/insights` (top movies by avg rating or review count)
### 6. Required Deliverable Files
- [X] Write `queries.sql` with all queries + comments
- [X] Write `perf.txt` (add 2+ indexes, measure query time before/after)
  - Indexes: `idx_review_movieid`, `idx_review_movieid_rating` on Review table
  - Insights query: 79.9 ms → 9.05 ms (~88% faster)
- [ ] Write `security.txt`
- [ ] Write `datasource.txt`
- [ ] Write `db_design.pdf` (ER model + normalization)
- [ ] Write `prelim.pdf`
- [ ] Record demo video
