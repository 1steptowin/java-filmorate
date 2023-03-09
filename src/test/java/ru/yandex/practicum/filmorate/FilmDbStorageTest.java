package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.MpaGenreNotFoundExeption;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmStorage storage;
    public static Film createFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("film1");
        film.setDescription("film1desc");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2022,01,01));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        ArrayList<Genre> genres = new ArrayList<>();
        film.setGenres(genres);
        film.setMpa(mpa);
        return film;
    }

    @BeforeAll
    void createTestFilm() throws SqlUpdateException {
        storage.create(createFilm());
    }
    @AfterEach
    void updateFilm() throws InvalidFilmId {
        storage.update(createFilm());
    }

    @Test
    public void testFindAll() throws InvalidFilmId {
        Film film = createFilm();
        ArrayList<Film> list = new ArrayList<>();
        list.add(film);
        Assert.assertThat(list,equalTo(storage.findAll()));
    }

    @Test
    public void testUpdateFilm() throws InvalidFilmId {
        Film film = createFilm();
        film.setName("updatefilm");
        storage.update(film);
        Assert.assertThat(film,equalTo(storage.getFilm(1)));
    }
    @Test
    public void testGetFilm () throws InvalidFilmId {
        Film film = createFilm();
        Assert.assertThat(film,equalTo(storage.getFilm(1)));
    }
    @Test
    public void testFindGenres () {
        ArrayList<Genre> list = new ArrayList<>();
        Genre genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("Комедия");
        Genre genre2 = new Genre();
        genre2.setId(2);
        genre2.setName("Драма");
        Genre genre3 = new Genre();
        genre3.setId(3);
        genre3.setName("Мультфильм");
        Genre genre4 = new Genre();
        genre4.setId(4);
        genre4.setName("Триллер");
        Genre genre5 = new Genre();
        genre5.setId(5);
        genre5.setName("Документальный");
        Genre genre6 = new Genre();
        genre6.setId(6);
        genre6.setName("Боевик");
        list.add(genre1);
        list.add(genre2);
        list.add(genre3);
        list.add(genre4);
        list.add(genre5);
        list.add(genre6);
        Assert.assertThat(list,equalTo(storage.findGenres()));
    }
    @Test
    public void testGetGenre() throws MpaGenreNotFoundExeption {
        Genre genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("Комедия");
        Assert.assertThat(genre1,equalTo(storage.findGenre(1)));
    }
    @Test
    public void testFindMPAs() {
        Mpa mpa1 = new Mpa();
        mpa1.setName("G");
        mpa1.setId(1);
        Mpa mpa2 = new Mpa();
        mpa2.setName("PG");
        mpa2.setId(2);
        Mpa mpa3 = new Mpa();
        mpa3.setName("PG-13");
        mpa3.setId(3);
        Mpa mpa4 = new Mpa();
        mpa4.setName("R");
        mpa4.setId(4);
        Mpa mpa5 = new Mpa();
        mpa5.setName("NC-17");
        mpa5.setId(5);
        ArrayList<Mpa> list = new ArrayList<>();
        list.add(mpa1);
        list.add(mpa2);
        list.add(mpa3);
        list.add(mpa4);
        list.add(mpa5);
        Assert.assertThat(list,equalTo(storage.findMpas()));
    }
    @Test
    public void testFindMPA() throws MpaGenreNotFoundExeption {
        Mpa mpa1 = new Mpa();
        mpa1.setName("G");
        mpa1.setId(1);
        Assert.assertThat(mpa1,equalTo(storage.findMpa(1)));
    }

}
