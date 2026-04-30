# NAME'M MOVIES

Group Name: [GROUP NAME]

---

## Team Members

**[Member 1 Name]**
- 

**[Member 2 Name]**
- 

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

## How to Run

1. Start the MySQL Docker container
2. Run the schema: execute `ddl.sql` against `demodb`
3. Load data: execute `data.sql` against `demodb`
4. Start the app: `./mvnw spring-boot:run`
5. Open `http://localhost:8080/login`

---

## Submission Checklist

- [ ] `prelim.pdf` — title, problem description, solution overview, ER diagram, technologies
- [ ] `db_design.pdf` — ER model, ER-to-relation conversion, functional dependencies, normalization to BCNF/3NF
- [ ] `ddl.sql` — all CREATE TABLE statements
- [ ] `data.sql` — all INSERT statements to populate the demo database
- [ ] `datasource.txt` — description and link to the TMDB dataset
- [ ] `queries.sql` — all SQL queries used in the app with comments and URL paths
- [ ] `perf.txt` — index creation statements, affected queries, timing before/after
- [ ] `security.txt` — description of BCrypt password hashing approach
- [ ] Demo video
- [ ] Working code
- [ ] `readme.txt` (this file, plain text copy for submission)
- [ ] Group member contribution form (signed)

---

## Query Requirements Tracker

Minimum: 6 independent SQL statements (retrieval, insertion, deletion, update)
Minimum: 3 queries with aggregation or joins

| # | Query | Type | Has Join/Agg | Page |
|---|-------|------|--------------|------|
| 1 | SELECT user by username (login) | retrieval | | /login |
| 2 | INSERT new user (registration) | insertion | | /createAccount |
| 3 | | | | |
| 4 | | | | |
| 5 | | | | |
| 6 | | | | |

---

## TODO (in order)

### 1. Database
- [ ] Finalize and run `ddl.sql`
- [ ] Load TMDB movie data into Movie/Genre tables (`data.sql`)
- [ ] Ensure Movie table has 1000+ rows

### 2. Fix Auth
- [ ] Handle duplicate username gracefully in `accountController`

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
- [ ] Movie detail + reviews page `/movies/{id}`

### 6. Required Deliverable Files
- [ ] Write `queries.sql` with all queries + comments
- [ ] Write `perf.txt` (add 2+ indexes, measure query time before/after)
- [ ] Write `security.txt`
- [ ] Write `datasource.txt`
- [ ] Write `db_design.pdf` (ER model + normalization)
- [ ] Write `prelim.pdf`
- [ ] Record demo video
