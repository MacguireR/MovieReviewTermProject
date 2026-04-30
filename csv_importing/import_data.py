import csv
import json
from datetime import datetime

CSV_FILE = 'tmdb_5000_movies.csv'
OUTPUT_FILE = '../src/main/resources/data.sql'

def escape(value):
    return value.replace("'", "''") if value else ''

def parse_date(raw):
    if not raw or not raw.strip():
        return None
    try:
        return datetime.strptime(raw.strip(), '%m/%d/%y').strftime('%Y-%m-%d')
    except ValueError:
        return None

genres = {}       # name -> assigned genreID
genre_counter = 1

movies = []       # list of (movieID, title, releaseDate, tagline)
movie_genres = [] # list of (movieID, genreID)

with open(CSV_FILE, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    movie_id = 1
    for row in reader:
        title = escape(row.get('title', ''))
        if not title:
            continue

        tagline_raw = row.get('tagline', '').strip()
        tagline = escape(tagline_raw) if tagline_raw else None
        release_date = parse_date(row.get('release_date', ''))

        try:
            genre_list = json.loads(row.get('genres', '[]'))
        except json.JSONDecodeError:
            genre_list = []

        for g in genre_list:
            gname = g.get('name', '').strip()
            if not gname:
                continue
            if gname not in genres:
                genre_counter_val = len(genres) + 1
                genres[gname] = genre_counter_val
            movie_genres.append((movie_id, genres[gname]))

        movies.append((movie_id, title, release_date, tagline))
        movie_id += 1

with open(OUTPUT_FILE, 'w', encoding='utf-8') as out:
    out.write('-- Genre seed data\n')
    for name, gid in sorted(genres.items(), key=lambda x: x[1]):
        out.write(f"INSERT INTO Genre (genreID, name) VALUES ({gid}, '{escape(name)}');\n")

    out.write('\n-- Movie seed data\n')
    for mid, title, release_date, tagline in movies:
        date_val = f"'{release_date}'" if release_date else 'NULL'
        tag_val = f"'{tagline}'" if tagline else 'NULL'
        out.write(
            f"INSERT INTO Movie (movieID, title, releaseDate, tagline) "
            f"VALUES ({mid}, '{title}', {date_val}, {tag_val});\n"
        )

    out.write('\n-- MovieGenre relationships\n')
    for mid, gid in movie_genres:
        out.write(f"INSERT INTO MovieGenre (movieID, genreID) VALUES ({mid}, {gid});\n")

print(f"Done: {len(movies)} movies, {len(genres)} genres, {len(movie_genres)} genre links")
print(f"Output written to {OUTPUT_FILE}")
