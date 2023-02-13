package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data @Slf4j @FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
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
    Rating rating;
    @NonFinal
    List<Genre> genres;
}
