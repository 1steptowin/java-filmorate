package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }
    public void addLike(Film film, User user) throws InvalidFilmId {
        Set<Integer> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);
        storage.update(film);
    }

    public void deleteLike(Film film, User user ) throws InvalidFilmId {
        Set<Integer> likes = film.getLikes();
        likes.remove(user.getId());
        film.setLikes(likes);
        storage.update(film);
    }

    public ArrayList<Film> topFilms(int count) {
        ArrayList<Film> films = storage.findAll();
        films.sort(new SortByLikes());
        ArrayList<Film> sort = new ArrayList<>();
        for (int i = 0; i<(count -1); i++) {
            Film filmsSort = films.get(i);
            sort.add(filmsSort);
        }
        return sort;
    }
    static class SortByLikes implements Comparator<Film> {
        public int compare (Film film1, Film film2) {
            return film1.getLikes().size() - film2.getLikes().size();
        }
    }
}
