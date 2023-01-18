package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Не прошла валидация")
public class ValidationException extends Exception{
    public ValidationException(String s) {
        super(s);
    }
}