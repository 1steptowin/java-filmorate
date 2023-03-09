package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Not found")
public class MpaGenreNotFoundExeption extends Exception{
    public MpaGenreNotFoundExeption(String s) {
        super(s);
    }
}
