package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import java.time.LocalDate;

@Data @FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class User {
    @NonFinal
    int id;
    @Email
    String email;
    @NotNull
    String login;
    @NonFinal
    String name;
    LocalDate birthday;
}