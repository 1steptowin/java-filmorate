package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    FilmService service;

    @Autowired
    public FilmController( FilmService service) {

        this.service = service;
    }
    @GetMapping(StaticPaths.FILM_PATH)
    public ArrayList<Film> findAll() throws InvalidFilmId {
        return service.findAll();
    }

    @PostMapping(StaticPaths.FILM_PATH)
    public Film create(@RequestBody Film film) throws ValidationException, SqlUpdateException, InvalidFilmId {
        return service.create(film);
    }
    @PutMapping(StaticPaths.FILM_PATH)
    public Film update(@RequestBody Film film) throws ValidationException, InvalidFilmId {
        return service.update(film);
    }
    @GetMapping(StaticPaths.FILM_PATH+"/{id}")
    public Film findById(@PathVariable int id) throws InvalidFilmId {
        return service.findById(id);
    }
    @PutMapping(StaticPaths.FILM_PATH+"/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id,@PathVariable int userId) throws InvalidFilmId, InvalidUserId {
        service.addLike(id,userId);
    }
    @DeleteMapping(StaticPaths.FILM_PATH+"/{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable int id,@PathVariable int userId) throws InvalidFilmId, InvalidUserId {
        service.deleteLike(id,userId);
    }

    @GetMapping(StaticPaths.FILM_PATH+"/popular")
    public ArrayList<Film> topFilms(@RequestParam(defaultValue = "10") int count) throws InvalidFilmId {
        return service.topFilms(count);
    }

    @GetMapping("/genres")
    public ArrayList<Genre> findGenres() {
        return service.findGenres();
    }
    @GetMapping("/genres/{id}")
    public Genre findGenre(@PathVariable int id) throws MpaGenreNotFoundExeption {
        return service.findGenre(id);
    }

    @GetMapping("/mpa")
    public ArrayList<Mpa> findMpas() {
        return service.findMpas();
    }
    @GetMapping("/mpa/{id}")
    public Mpa findMpa(@PathVariable int id) throws MpaGenreNotFoundExeption {
        return service.findMpa(id);
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
