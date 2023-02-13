package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findAll();

    void create(User user) throws SqlUpdateException;

    void update(User user) throws InvalidUserId;

    User getUser(int id) throws InvalidUserId;

    void addFriend(int id, int friendId);

    List<Integer> findFriends(int id);
}
