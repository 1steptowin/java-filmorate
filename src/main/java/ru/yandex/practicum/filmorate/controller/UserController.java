package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping(StaticPaths.USER_PATH)
    public List<User> findAll(){
        List<User> list = new ArrayList<>();
        for (User user: users.values()) {
        list.add(user);
        }
        return list;
    }

    @PostMapping(StaticPaths.USER_PATH)
    public User create(@Valid @RequestBody User user) throws ValidationException {
        try {
            if (validate(user)) {
                if (user.getName()==null) {
                    user.setName(user.getLogin());
                }
                user.setId(users.size()+1);
                users.put(users.size() + 1, user);
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
    public User update(@RequestBody User user) throws InvalidUserId, ValidationException {
        if (validate(user)) {

            for (Integer i : users.keySet()) {
                if (users.get(i).getId() == (user.getId())) {
                    if (user.getName().isEmpty()){
                        user.setName(user.getLogin());
                    }
                    users.put(i, user);
                    log.info("Пользователь обновлен", user);
                    return users.get(i);
                }
            }
            log.warn("Ошибка обновления пользователя", user);
            throw new InvalidUserId("Данного юзера не существует");
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
