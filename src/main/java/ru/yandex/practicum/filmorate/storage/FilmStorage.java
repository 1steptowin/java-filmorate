package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
ArrayList<Film> findAll();

void create(Film film);

Film update(Film film) throws InvalidFilmId;

}
