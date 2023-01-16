package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final HashMap<Integer,Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll(){
        List<Film> list = new ArrayList<>();
        for (Film film: films.values()) {
            list.add(film);
        }
        return list;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if (validate(film)){
            film.setId(films.size()+1);
            films.put(films.size()+1, film);
            log.info("Фильм "+film.getName()+" добавлен", film);
            return film;
        } else {
            log.warn("Фильм не прошел валидацию", film);
            throw new ValidationException("Не прошла валидация");
        }
    }
    @PutMapping("/films")
    public Film update(@RequestBody Film film) throws InvalidFilmId, ValidationException {
        if (validate(film)) {
            for (Integer i : films.keySet()) {
                if (films.get(i).getId() == (film.getId())) {
                    films.put(i, film);
                    log.info("Фильм " + film.getName() + " обновлен", film);
                    return films.get(i);
                }
            }
            log.warn("Ошибка при обновлении фильма", film);
            throw new InvalidFilmId("Такого фильма не существует");
        } else {
            log.warn("Ошибка валидации фильма",film);
            throw new ValidationException("Ошибка валидации");
        }
    }
    private Boolean validate(Film film) throws ValidationException {
        try {
            if (film.getDescription().length()<=200&
                    film.getReleaseDate().isAfter(LocalDate.of(1895,12,28))&
                    !(film.getDuration()<=0)&
                    !film.getName().isEmpty()){
                return true;
            } else
                return false;
        } catch (NullPointerException e) {
            log.warn("Пришел пустой запрос");
            throw new ValidationException("Пришел пустой запрос");
        }
        }

}
