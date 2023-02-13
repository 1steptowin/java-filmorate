package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.HashMap;

@Data
public class User {
    @NonFinal
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    HashMap<Integer, Boolean> friends;

}
