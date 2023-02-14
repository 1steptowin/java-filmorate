package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.NonFinal;

@Data
public class Mpa {
    @NonFinal
    int id;
    @NonFinal
    String name;
}
