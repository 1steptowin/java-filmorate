# java-filmorate
Template repository for Filmorate project.

![Alt](/er_db.png "er diagramm")

Examples SQL:
1. to get User info: 
SELECT * FROM USER WHERE user_id = 1;
2. to get genres for film:
SELECT genre.id,genre.name FROM film INNER JOIN film_genre ON film.id = film_genre.film_id INNER JOIN genre ON film_genre.genre_id = genre.id WHERE film.id = 1;
3. to get friends for user:
SELECT friend_id FROM friendship WHERE user_id = 1 AND status = true