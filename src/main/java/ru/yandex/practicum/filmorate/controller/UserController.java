package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    UserService service;
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping(StaticPaths.USER_PATH)
    public ArrayList<User> findAll() throws InvalidUserId {
        return service.findAll();
    }

    @PostMapping(StaticPaths.USER_PATH)
    public User create(@Valid @RequestBody User user) throws ValidationException, SqlUpdateException {
        return service.createUser(user);
    }
    @PutMapping(StaticPaths.USER_PATH)
    public User update(@RequestBody User user) throws ValidationException, InvalidUserId, SqlUpdateException {
        return service.updateUser(user);
    }

    @GetMapping(StaticPaths.USER_PATH+"/{id}")
    public User findById(@PathVariable int id) throws InvalidUserId {
        return service.findById(id);
    }

    @PutMapping(StaticPaths.USER_PATH +"/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        service.addFriend(id,friendId);
    }

    @DeleteMapping(StaticPaths.USER_PATH+"/{id}/friends/{friendId}")
    public Map<User,User> deleteFriend(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        return service.deleteFriend(id,friendId);
    }

    @GetMapping(StaticPaths.USER_PATH + "/{id}/friends")
    public List<User> getFriendList (@PathVariable int id) throws InvalidUserId {
       return service.findFriends(id);
    }

    @GetMapping(StaticPaths.USER_PATH +"/{id}/friends/common/{friendId}")
    public Set<User> findMutualFriends(@PathVariable int id, @PathVariable int friendId) throws InvalidUserId {
        return service.mutualFriends(id,friendId);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoUserId(final InvalidUserId e) {
        return new ResponseEntity<>(Map.of("error",e.getMessage()),
                HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleSqlException(final SqlUpdateException e) {
        return new ResponseEntity<>(Map.of("error",e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidation(final  ValidationException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),HttpStatus.BAD_REQUEST);
    }
}

