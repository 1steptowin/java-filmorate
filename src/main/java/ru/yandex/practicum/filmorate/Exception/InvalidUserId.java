package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Не найдем id пользователя")
public class InvalidUserId extends Exception{
    public InvalidUserId(String s) {
        super(s);
    }
}
