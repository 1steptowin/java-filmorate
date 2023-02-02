package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {

    InMemoryFilmStorage storage;
    FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }
    @GetMapping(StaticPaths.FILM_PATH)
    public ArrayList<Film> findAll(){
        return storage.findAll();
    }

    @PostMapping(StaticPaths.FILM_PATH)
    public Film create(@RequestBody Film film) throws ValidationException {
        if (service.validate(film)){
            storage.create(film);
            log.info("Фильм "+film.getName()+" добавлен");
            return film;
        } else {
            log.warn("Фильм не прошел валидацию");
            throw new ValidationException("Не прошла валидация");
        }
    }
    @PutMapping(StaticPaths.FILM_PATH)
    public Film update(@RequestBody Film film) throws ValidationException, InvalidFilmId {
        if (service.validate(film)) {
           try {
               return storage.update(film);
           } catch (InvalidFilmId e) {
               log.warn("Ошибка при обновлении фильма");
               throw new InvalidFilmId("Фильм не найден");
           }
        } else {
            log.warn("Ошибка валидации фильма");
            throw new ValidationException("Ошибка валидации");
        }
    }
    @GetMapping(StaticPaths.FILM_PATH+"/{id}")
    public Optional<Film> findById(@PathVariable int id) throws InvalidFilmId {
        if (storage.getFilm(id) != null) {
            log.info("Получен запрос findById");
            return storage.findAll().stream()
                    .filter(x -> x.getId() == id)
                    .findFirst();
        }
        else {
            log.warn("Ошибка валидации");
            throw new InvalidFilmId("Такого фильма не существует");
        }
    }
    @PutMapping(StaticPaths.FILM_PATH+"/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id,@PathVariable int userId) throws InvalidFilmId {
        if (storage.getFilm(id) != null) {
            service.addLike(storage.getFilm(id), userId);
            log.info("Пользователь "+userId+" добавил лайк к фильму "+id);
            return storage.getFilm(id);
        } else {
            log.warn("Попытка добавить лайк в несуществующий фильм");
            throw new InvalidFilmId("Такого фильма не существует");
        }
    }
    @DeleteMapping(StaticPaths.FILM_PATH+"/{id}/like/{userId}")
    public Film deleteFilmLike(@PathVariable int id,@PathVariable int userId) throws InvalidFilmId, InvalidUserId {
        if (storage.getFilm(id) != null) {
            service.deleteLike(storage.getFilm(id), userId);
            log.info("Пользователь "+userId+" удалил лайк к фильму "+id);
            return storage.getFilm(id);
        } else {
            log.warn("Попытка удалить лайк у несуществующего фильма");
            throw new InvalidFilmId("Такого фильма не существует");
        }
    }

    @GetMapping(StaticPaths.FILM_PATH+"/popular")
    public ArrayList<Film> topFilms(@RequestParam(defaultValue = "10") int count) {
        if (!storage.findAll().isEmpty()) {
            log.info("Получен запрос topFilms");
            return service.topFilms(count);
        }
        log.warn("Попытка выгрузить пустой список");
        return null;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoFilmId(final InvalidFilmId e) {
        return new ResponseEntity<>(Map.of("error",e.getMessage()),
                HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidation(final  ValidationException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),HttpStatus.BAD_REQUEST);
    }


}
