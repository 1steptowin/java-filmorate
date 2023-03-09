package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class FilmService {
    private final FilmStorage storage;
    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public ArrayList<Film> findAll() throws InvalidFilmId {
        return storage.findAll();
    }

    public void addLike(int id, int userID) throws InvalidFilmId, InvalidUserId {
        if (storage.checkUser(userID)) {
            storage.addlike(id, userID);
        } else throw new InvalidUserId("Пользователь не существует");
    }

    public void deleteLike(int id, int userID ) throws InvalidFilmId, InvalidUserId {
        if (storage.checkUser(userID)) {
            storage.deleteLike(id, userID);
        } else throw new InvalidUserId("Пользователь не существует");
    }

    public ArrayList<Film> topFilms(int count) throws InvalidFilmId {
        if (storage.topFilms(count).isEmpty()) {
            return findAll();
        } else {
            return storage.topFilms(count);
        }
    }

    public Film create(Film film) throws ValidationException, SqlUpdateException, InvalidFilmId {
        if (validate(film)) {
            storage.create(film);
        } else throw new ValidationException("Ошибка валидации");
        return storage.getFilm(storage.getFilmByName(film.getName()));
    }

    public Film update(Film film) throws InvalidFilmId, ValidationException {
        if (validate(film)) {
            storage.update(film);
        } else throw new ValidationException("Ошибка валидации");
        return storage.getFilm(film.getId());
    }

    public Film findById(int id) throws InvalidFilmId {
       return storage.getFilm(id);
    }
    public Boolean validate(Film film) throws ValidationException {
        try {
            return film.getDescription().length() <= 200 &
                    film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) &
                    !(film.getDuration() <= 0) &
                    !film.getName().isEmpty();
        } catch (NullPointerException e) {
            //log.warn("Пришел пустой запрос");
            throw new ValidationException("Пришел пустой запрос");
        }
    }

    public ArrayList<Genre> findGenres() {
        return storage.findGenres();
    }

    public Genre findGenre(int id) throws MpaGenreNotFoundExeption {
        return storage.findGenre(id);
    }

    public ArrayList<Mpa> findMpas() {
        return storage.findMpas();
    }

    public Mpa findMpa(int id) throws MpaGenreNotFoundExeption {
        return storage.findMpa(id);
    }
}
