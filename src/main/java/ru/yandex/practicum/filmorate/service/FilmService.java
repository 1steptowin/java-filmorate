package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage storage;
    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
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
    public void addLike(Film film, int userID) throws InvalidFilmId {
        try {
            Set<Integer> likes = film.getLikes();
            likes.add(userID);
            film.setLikes(likes);
            storage.update(film);
        } catch (NullPointerException e) {
            Set<Integer> likes = new HashSet<>();
            likes.add(userID);
            film.setLikes(likes);
            storage.update(film);
        }

    }

    public void deleteLike(Film film, int userID ) throws InvalidFilmId, InvalidUserId {
        try {
            Set<Integer> likes = film.getLikes();
            likes.remove(userID);
            film.setLikes(likes);
            storage.update(film);
        } catch (NullPointerException e) {
            throw new InvalidUserId("Такого юзера нет");
        }

    }

    public ArrayList<Film> topFilms(int count) {
        if (storage.findAll().size()!=0) {
            ArrayList<Film> films = storage.findAll();
            films.sort(new SortByLikes());
            ArrayList<Film> sort = new ArrayList<>();
            if (count<films.size()) {
                for (int i = 0; i < (count); i++) {
                    Film filmsSort = films.get(i);
                    sort.add(filmsSort);
                }
                return sort;
            } else {
                count = films.size();
                for (int i = 0; i < (count); i++) {
                    Film filmsSort = films.get(i);
                    sort.add(filmsSort);
                }
                return sort;
            }
        }
        return null;
    }

}
