package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data @FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    @NonFinal
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    @NonFinal
    Set<Integer> likes;
    @NonFinal
    List<Genre> genres;
    Mpa mpa;
}
