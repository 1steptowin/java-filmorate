package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.HashMap;

@Data @FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    @NonFinal
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @NonFinal
    HashMap<Integer, Boolean> friends;

}
