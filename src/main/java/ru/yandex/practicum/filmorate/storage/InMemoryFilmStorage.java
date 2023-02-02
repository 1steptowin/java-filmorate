package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void create(Film film) {
        film.setId(films.size()+1);
        films.put(films.size()+1, film);
    }

    @Override
    public Film update(Film film) throws InvalidFilmId {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(),film);
            return film;
        }
        else {
            throw new InvalidFilmId("Фильм не найден");
        }
    }
    public Film getFilm (int id) {
        return films.get(id);
    }
}
