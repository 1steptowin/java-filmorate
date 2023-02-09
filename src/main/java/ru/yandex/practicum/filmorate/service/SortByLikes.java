package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

 class SortByLikes implements Comparator<Film> {
    public int compare (Film film1, Film film2) {
        int value1 = 0;
        int value2 = 0;
        try {
            value1 = film1.getLikes().size();
        } catch (NullPointerException e) {

        }
        try {
            value2 = film2.getLikes().size();
        } catch (NullPointerException e) {
        }
        return value2 - value1;
    }
}