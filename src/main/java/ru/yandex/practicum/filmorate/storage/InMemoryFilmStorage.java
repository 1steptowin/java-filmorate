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
        for (Integer i : films.keySet()) {
            if (films.get(i).getId() == (film.getId())) {
                films.put(i, film);
                return films.get(i);
            }
        }
        throw new InvalidFilmId("Фильм не найден");
    }
}
