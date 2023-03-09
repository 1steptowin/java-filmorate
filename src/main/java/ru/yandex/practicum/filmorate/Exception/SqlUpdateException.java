package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY,reason = "Ошибка update запроса")
public class SqlUpdateException extends Exception{
    public SqlUpdateException(String s) {
        super(s);
    }
}
