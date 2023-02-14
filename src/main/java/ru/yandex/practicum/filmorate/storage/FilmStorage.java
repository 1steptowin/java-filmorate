package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.MpaGenreNotFoundExeption;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;

public interface FilmStorage {
ArrayList<Film> findAll() throws InvalidFilmId;

void create(Film film) throws SqlUpdateException;

Film update(Film film) throws InvalidFilmId;


void addlike(int id, int userId);

ArrayList<Film> topFilms(int count) throws InvalidFilmId;

int getFilmByName(String name) throws SqlUpdateException;

Film getFilm(int id) throws InvalidFilmId;

void deleteLike(int id,int userId);

    Boolean checkUser(int id);

    ArrayList<Genre> findGenres();

    Genre findGenre(int id) throws MpaGenreNotFoundExeption;

    ArrayList<Mpa> findMpas();

    Mpa findMpa(int id) throws MpaGenreNotFoundExeption;
}
