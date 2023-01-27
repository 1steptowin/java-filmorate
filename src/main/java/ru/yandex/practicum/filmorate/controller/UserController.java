package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@RestController
public class UserController {

    InMemoryUserStorage storage;
    @Autowired
    public UserController(InMemoryUserStorage storage) {
        this.storage = storage;
    }
    @GetMapping(StaticPaths.USER_PATH)
    public ArrayList<User> findAll(){
        return storage.findAll();
    }

    @PostMapping(StaticPaths.USER_PATH)
    public User create(@Valid @RequestBody User user) throws ValidationException {
        try {
            if (validate(user)) {
                storage.create(user);
                log.info("Добавлен пользователь",user);
                return user;
            } else {
                log.warn("Ошибка валидации пользователя");
                throw new ValidationException("Ошибка валидации user");
            }
        } catch (NullPointerException e) {
            log.warn("Ошибка отправки запроса",user);
            throw new ValidationException("Пришло пустое поле");
        }

    }
    @PutMapping(StaticPaths.USER_PATH)
    public User update(@RequestBody User user) throws  ValidationException {
        if (validate(user)) {
            try {
                return storage.update(user);
            } catch (InvalidUserId e) {
                log.warn("Ошибка обновления пользователя", user);
                return user;
            }
        }
        else {
            log.warn("шибка валидации юзера", user);
            throw new ValidationException("Ошибка валидации юзера");
        }
    }

    private Boolean validate(User user) throws ValidationException {
        try {
            if (!user.getEmail().isEmpty()&
                    user.getEmail().contains("@")&
                    !user.getLogin().isEmpty()&
                    !user.getLogin().contains(" ")&
                    !user.getBirthday().isAfter(LocalDate.now())) {
                return true;
            } else {
                throw new ValidationException("Не прошла валидация");
                //return false;
            }
        } catch (NullPointerException e) {
            log.info("Пришел запрос с пустыми полями");
            throw new ValidationException("Пришел запрос с пустыми полями");
        }
    }
}
