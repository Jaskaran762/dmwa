/*

use lab_1;
show tables;

/*count of directors  present in iMDB
----------using count keyword-------*/
SELECT count(*) FROM directors;

/*count of movies released post-year 2000
----------using count keyword-------*/
#---------including movies released in year 2000------------
SELECT count(*) FROM movies WHERE movies.year >= 2000;
#---------not including movies released in year 2000------------
SELECT count(*) FROM movies WHERE movies.year > 2000;


/*list of genres of movies directed by Andrew Adamson
----------using sub-query-----------*/
SELECT genre FROM directors_genres WHERE director_id IN 
(SELECT id FROM directors WHERE LOWER(concat(first_name,' ', last_name)) = LOWER('Andrew Adamson'));
/*----------using join--------------*/
SELECT G.genre FROM directors_genres G INNER JOIN directors D ON G.director_id = D.id AND LOWER(concat(first_name,' ', last_name)) = LOWER('Andrew Adamson');


/*directors whose movies are ranked between 7 to 8 ranking
----------using sub-query-----------*/
SELECT * FROM directors WHERE id IN 
(SELECT director_id FROM movies_directors WHERE movie_id IN
(SELECT id FROM movies WHERE movies.rank>=7 and movies.rank<=8));
/*----------using join--------------*/
SELECT D.*,M.name, M.rank FROM directors D INNER JOIN movies_directors MD ON D.id = MD.director_id INNER JOIN movies M ON MD.movie_id = M.id WHERE M.rank>=7 AND M.rank<=8;


/*role of Julliet Akinyi in Lost in Translation movie
----------------using join-------------*/
SELECT R.* FROM movies M INNER JOIN roles R ON M.id = R.movie_id and UPPER(M.name) = UPPER('Lost in Translation') INNER JOIN  actors A ON R.actor_id = A.id AND UPPER(concat(A.first_name,' ',A.last_name)) = UPPER('Julliet Akinyi');
/*----------------using subquery---------*/
SELECT R.* FROM roles R WHERE 
R.actor_id IN ( SELECT id FROM actors WHERE UPPER(concat(first_name,' ',last_name)) = UPPER('Julliet Akinyi')) AND
R.movie_id IN ( SELECT id FROM movies WHERE UPPER(name) = UPPER('Lost in Translation')) AND
R.role IS NOT null;


/*movies that contain the letter ‘o' in any position
-----------------using like keyword-------*/
SELECT * FROM movies WHERE name LIKE '%o%';