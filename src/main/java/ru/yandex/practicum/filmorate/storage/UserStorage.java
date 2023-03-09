package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll() throws InvalidUserId;

    User create(User user) throws SqlUpdateException;

    User update(User user) throws InvalidUserId, SqlUpdateException;

    User getUser(int id) throws InvalidUserId;

    void addFriend(int id, int friendId);

    List<Integer> findFriends(int id) throws InvalidUserId;

    void delete(int id, int friendId);
}
