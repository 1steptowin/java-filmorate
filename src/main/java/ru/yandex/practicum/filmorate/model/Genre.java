package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Data @RequiredArgsConstructor
public class Genre {
    @NonFinal
    int id;
    @NonFinal
    String name;
}
