package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Не найден id фильма")
public class InvalidFilmId extends Exception{
    public InvalidFilmId(String s) {
        super(s);
    }
}
