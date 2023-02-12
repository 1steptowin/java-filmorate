package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Slf4j
@RestController
public class UserController {

    InMemoryUserStorage storage;
    UserService service;
    @Autowired
    public UserController(InMemoryUserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }
    @GetMapping(StaticPaths.USER_PATH)
    public ArrayList<User> findAll(){
        return storage.findAll();
    }

    @PostMapping(StaticPaths.USER_PATH)
    public User create(@Valid @RequestBody User user) throws ValidationException {
        try {
            if (service.validate(user)) {
                storage.create(user);
                log.info("Добавлен пользователь");
                return user;
            } else {
                log.warn("Ошибка валидации пользователя");
                throw new ValidationException("Ошибка валидации user");
            }
        } catch (NullPointerException e) {
            log.warn("Ошибка отправки запроса");
            throw new ValidationException("Пришло пустое поле");
        }

    }
    @PutMapping(StaticPaths.USER_PATH)
    public User update(@RequestBody User user) throws ValidationException, InvalidUserId {
        if (service.validate(user)) {
            try {
                return storage.update(user);
            } catch (InvalidUserId e) {
                log.warn("Ошибка обновления пользователя");
                throw new InvalidUserId("Пользователь не найден");
            }
        }
        else {
            log.warn("Ошибка валидации пользователя");
            throw new ValidationException("Ошибка валидации юзера");
        }
    }

    @GetMapping(StaticPaths.USER_PATH+"/{id}")
    public Optional<User> findById(@PathVariable int id) throws InvalidUserId {
        if (storage.getUser(id) != null) {
            log.info("Получен запрос userController.findById");
            return storage.findAll().stream()
                    .filter(x -> x.getId() == id)
                    .findFirst();
        } else {
            log.warn("Ошибка валидации");
            throw new InvalidUserId("Некорректный user id" + id);
        }
    }

    @PutMapping(StaticPaths.USER_PATH +"/{id}/friends/{friendId}")
    public Map<User,User> addFriend(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        if (storage.getUser(id) != null & storage.getUser(friendId) != null) {
            service.addFriend(storage.getUser(id), storage.getUser(friendId));
            log.info(storage.getUser(id).getName() + " подружился с "+ storage.getUser(friendId).getName());
            return Map.of(storage.getUser(id), storage.getUser(friendId));
        } else {
            throw new InvalidUserId("Какого-то пользователя не существует");
        }
    }

    @PutMapping(StaticPaths.USER_PATH+"/{id}/friends/{friendId}/accept")
    public Map<User,User> acceptFriend(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        if (storage.getUser(id) != null & storage.getUser(friendId) != null) {
            service.acceptFriend(storage.getUser(id), storage.getUser(friendId));
            log.info(storage.getUser(id).getName() + " принял заявку в друзья от "+ storage.getUser(friendId).getName());
            return Map.of(storage.getUser(id), storage.getUser(friendId));
        } else {
            throw new InvalidUserId("Какого-то пользователя не существует");
        }
    }
    @DeleteMapping(StaticPaths.USER_PATH+"/{id}/friends/{friendId}")
    public Map<User,User> deleteFriend(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        if (storage.getUser(id) != null & storage.getUser(friendId) != null) {
            service.deleteFriend(storage.getUser(id),storage.getUser(friendId));
            log.info("Больше не дружат: "+ storage.getUser(id).getName() + storage.getUser(friendId).getName());
            return Map.of(storage.getUser(id),storage.getUser(friendId));
        } else {
            log.warn("Неверный запрос deleteFriend");
            throw new InvalidUserId("Какого-то пользователя не существует");
        }
    }

    @GetMapping(StaticPaths.USER_PATH + "/{id}/friends")
    public List<User> getFriendList (@PathVariable int id) throws InvalidUserId {

        if (storage.getUser(id) != null) {
            if (storage.getUser(id).getFriends() != null) {
                List<User> friends = new ArrayList<>();

                for (Integer i : storage.getUser(id).getFriends()) {
                    friends.add(storage.getUser(i));
                }
                return friends;
            } else
                return null;
        } else throw new InvalidUserId("Некорректный id пользователя");
    }

    @GetMapping(StaticPaths.USER_PATH +"/{id}/friends/common/{friendId}")
    public Set<User> findMutualFriends(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        if (storage.getUser(id) != null & storage.getUser(friendId) != null) {
            return service.mutualFriends(storage.getUser(id), storage.getUser(friendId));
        } else {
            throw new InvalidUserId("Некорректный id пользователя");
        }
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoUserId(final InvalidUserId e) {
        return new ResponseEntity<>(Map.of("error",e.getMessage()),
                HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidation(final  ValidationException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),HttpStatus.BAD_REQUEST);
    }
}

