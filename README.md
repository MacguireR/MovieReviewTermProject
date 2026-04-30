# NAME'M MOVIES

A Spring Boot + MySQL web app for browsing movies and writing reviews.

---

## Database Schema

```sql
CREATE TABLE Genre (
    genreID     INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Movie (
    movieID     INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    releaseDate DATE,
    director    VARCHAR(255),
    rating      DECIMAL(3,1),
    genreID     INT,
    FOREIGN KEY (genreID) REFERENCES Genre(genreID)
);

CREATE TABLE User (
    userID   INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Review (
    reviewID INT PRIMARY KEY AUTO_INCREMENT,
    rating   INT NOT NULL,
    comment  TEXT,
    date     DATE NOT NULL,
    userID   INT,
    movieID  INT,
    FOREIGN KEY (userID)  REFERENCES User(userID),
    FOREIGN KEY (movieID) REFERENCES Movie(movieID)
);
```

---

## TODO (in order)

### 1. Database Setup
- [ ] Create the MySQL database and run the schema above
- [ ] Migrate existing `users` table to match the `User` schema (column `password_hash` → `password`)
- [ ] Add seed data: a few genres and movies to test with

### 2. Fix Authentication
- [ ] Add duplicate username error handling in `accountController` (currently throws a raw DB exception)

### 3. Backend — Movies & Genres
- [ ] `GenreController`: GET `/genres` — list all genres; GET `/genres/{id}` — movies in that genre
- [ ] `MovieController`: GET `/movies` — list all movies; GET `/movies/{id}` — movie detail with reviews

### 4. Backend — Reviews
- [ ] `ReviewController`: POST `/movies/{id}/review` — submit a review (requires login)

### 5. Frontend
- [ ] Genre list page (`/genres`)
- [ ] Movie list page (`/movies`) with genre filter
- [ ] Movie detail page (`/movies/{id}`) showing info + all reviews
- [ ] Review submission form on the movie detail page

### 6. Polish
- [ ] Input validation on all forms (backend)
- [ ] Add basic error pages (404, 500)

---

## What's Already Done

- User registration with BCrypt password hashing
- Login with session creation
- Logout with session invalidation
- Conditional nav (shows Genres/Logout when logged in, Login when not)
- Header/nav component with consistent styling
