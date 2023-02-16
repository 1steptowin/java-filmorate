package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.MpaGenreNotFoundExeption;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MPAMapper;

import java.util.ArrayList;
import java.util.List;
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public ArrayList<Film> findAll() throws InvalidFilmId {
        String sql = "SELECT id FROM film";
        List<Integer> list = jdbcTemplate.query(sql,new FilmMapper());
        ArrayList<Film> films = new ArrayList<>();
        for (Integer i: list) {
            films.add(getFilm(i));
        }
        return films;
    }

    @Override
    public void create(Film film) throws SqlUpdateException {
        String sql = "INSERT INTO film (name,description,realease_date,duration,rating_id) " +
                "values (?,?,?,?,?)";
        try {
            jdbcTemplate.update(sql,film.getName(),film.getDescription(),film.getReleaseDate(),film.getDuration(),film.getMpa().getId());
            log.info(String.format("Create film %s",film.getName()));
        } catch (Exception e) {
            log.warn("Ошибка создания фильма");
            throw new SqlUpdateException("Проблема с запросом");
        }
        int filmid = getFilmByName(film.getName());
        String sqlGenre = "INSERT INTO film_genre (film_id, genre_id)" +
                "VALUES(?,?)";
        if (film.getGenres()!=null) {
            for (Genre genre: film.getGenres()) {
                jdbcTemplate.update(sqlGenre,filmid,genre.getId());
            }
            log.info(String.format("Фильму %s добавлены жанры", film.getName()));
        }
    }

    @Override
    public Film update(Film film) throws InvalidFilmId {
        String sql = "UPDATE film SET name = ?, description = ?, realease_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql,film.getName(),film.getDescription(),film.getReleaseDate(),film.getDuration(),film.getMpa().getId(),film.getId());
            log.info(String.format("Film udapte %s",film.getName()));
            if (film.getGenres() != null) {
                try {
                    String sqlDeleteGenre = "DELETE FROM film_genre WHERE film_id = ?";
                    jdbcTemplate.update(sqlDeleteGenre,film.getId());
                } catch (Exception e) {
                    log.warn("Ошибка в методе update film");
                }
                String sqlGenre = "INSERT INTO film_genre (film_id,genre_id)" +
                        "VALUES(?,?)";
                for (Genre genre: film.getGenres()) {
                    try {
                        jdbcTemplate.update(sqlGenre,film.getId(),genre.getId());
                        log.info(String.format("Фильму %s добавлен жанр %d",film.getName(),genre.getId()));
                    } catch (Exception e) {
                        log.warn("Попытка добавить дубликат жанра");
                    }

                }
            }
            return getFilm(film.getId());
        } catch (Exception e) {
            throw new InvalidFilmId("Фильма не существует");
        }

    }


    @Override
    public void addlike(int id, int userId) {
        String sql = "INSERT INTO LIKES (film_id,user_id)" +
                "VALUES(?,?)";
        jdbcTemplate.update(sql,id,userId);
        log.info(String.format("Add like to film %d",id));
    }

    @Override
    public ArrayList<Film> topFilms(int count) throws InvalidFilmId {
        String sql = "SELECT id,count(likes.user_id) FROM film INNER JOIN likes ON film.id = likes.film_id GROUP BY film.id ORDER BY count(likes.user_id) DESC LIMIT ?";
        List<Integer> list = jdbcTemplate.query(sql,new FilmMapper(),count);
        ArrayList<Film> films = new ArrayList<>();
        for (Integer i: list) {
            films.add(getFilm(i));
        }
        return films;
    }

    @Override
    public int getFilmByName(String name) throws SqlUpdateException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT id FROM film WHERE name = ?",name);
        if (rowSet.next()) {
            return rowSet.getInt("id");
        } else throw new SqlUpdateException("Проблема в запросе");
    }
    @Override
    public Film getFilm(int id) throws InvalidFilmId {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("Select * FROM film  INNER JOIN rating ON film.rating_id = rating.id WHERE film.id = ?",id);
        if (rs.next()) {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("realease_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString(8));
            film.setMpa(mpa);
            try {
                String sql = "SELECT genre.id,genre.name FROM film INNER JOIN film_genre ON film.id = film_genre.film_id INNER JOIN genre ON film_genre.genre_id = genre.id WHERE film.id = ?";
                List<Genre> list = jdbcTemplate.query(sql,new GenreMapper(),film.getId());
                film.setGenres(list);
            } catch (Exception e) {
                System.out.println("Нет жанров");
            }

            return film;
        } else throw new InvalidFilmId("Фильма не существует");
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql,id,userId);
        log.info(String.format("Delete like from film %d",id));
    }

    @Override
    public Boolean checkUser(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,id);
        int userid = -1;
        if (rs.next()) {
            userid = rs.getInt(id);
        }
        if (userid == id) {
            return true;
        } else return false;
    }

    @Override
    public ArrayList<Genre> findGenres() {
        String sql = "SELECT * FROM GENRE";
        return (ArrayList<Genre>) jdbcTemplate.query(sql,new GenreMapper());
    }

    @Override
    public Genre findGenre(int id) throws MpaGenreNotFoundExeption {
        String sql = "SELECT * FROM genre WHERE id = ?";
        if (!jdbcTemplate.query(sql,new GenreMapper(),id).isEmpty()) {
            return jdbcTemplate.query(sql,new GenreMapper(),id).get(0);
        } else throw new MpaGenreNotFoundExeption("Not found");

    }

    @Override
    public ArrayList<Mpa> findMpas() {
        String sql = "SELECT * FROM rating";
        return (ArrayList<Mpa>) jdbcTemplate.query(sql,new MPAMapper());
    }

    @Override
    public Mpa findMpa(int id) throws MpaGenreNotFoundExeption {
        String sql = "SELECT * FROM rating WHERE id = ?";
        if (!jdbcTemplate.query(sql,new MPAMapper(),id).isEmpty()){
            return  jdbcTemplate.query(sql,new MPAMapper(),id).get(0);
        } else throw new MpaGenreNotFoundExeption("Not found");

    }
}
