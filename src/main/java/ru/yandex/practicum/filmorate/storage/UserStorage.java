package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    ArrayList<User> findAll();

    void create(User user);

    User update(User user) throws InvalidFilmId, InvalidUserId;
}
