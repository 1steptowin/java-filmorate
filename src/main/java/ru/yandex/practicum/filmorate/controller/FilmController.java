package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@RestController
public class FilmController {

    InMemoryFilmStorage storage;

    @Autowired
    public FilmController(InMemoryFilmStorage storage) {
        this.storage = storage;
    }
    @GetMapping(StaticPaths.FILM_PATH)
    public ArrayList<Film> findAll(){
        return storage.findAll();
    }

    @PostMapping(StaticPaths.FILM_PATH)
    public Film create(@RequestBody Film film) throws ValidationException {
        if (validate(film)){
            storage.create(film);
            log.info("Фильм "+film.getName()+" добавлен", film);
            return film;
        } else {
            log.warn("Фильм не прошел валидацию", film);
            throw new ValidationException("Не прошла валидация");
        }
    }
    @PutMapping(StaticPaths.FILM_PATH)
    public Film update(@RequestBody Film film) throws ValidationException {
        if (validate(film)) {
           try {
               return storage.update(film);
           } catch (InvalidFilmId e) {
               log.warn("Ошибка при обновлении фильма", film);
               return film;
           }
        } else {
            log.warn("Ошибка валидации фильма",film);
            throw new ValidationException("Ошибка валидации");
        }
    }
    private Boolean validate(Film film) throws ValidationException {
        try {
            return film.getDescription().length() <= 200 &
                    film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) &
                    !(film.getDuration() <= 0) &
                    !film.getName().isEmpty();
        } catch (NullPointerException e) {
            log.warn("Пришел пустой запрос");
            throw new ValidationException("Пришел пустой запрос");
        }
        }

}
