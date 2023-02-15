package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public ArrayList<User> findAll() throws InvalidUserId {
        return (ArrayList<User>) storage.findAll();
    }

    public User createUser(User user) throws ValidationException {
        if (validate(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
           try {
               return storage.create(user);
           } catch (SqlUpdateException e) {
               throw new ValidationException("Пользователь существует");
           }
        } else {
            throw new ValidationException("Ошибка валидации");
        }
    }

    public User updateUser(User user) throws ValidationException, InvalidUserId, SqlUpdateException {
        if (validate(user)) {
           return storage.update(user);
        } else throw new ValidationException("Ошибка валидации");
    }

    public User findById(int id) throws InvalidUserId {
        return storage.getUser(id);
    }


    public Boolean validate(User user) throws ValidationException {
        try {
            if (!user.getEmail().isEmpty()&
                    user.getEmail().contains("@")&
                    !user.getLogin().isEmpty()&
                    !user.getLogin().contains(" ")&
                    !user.getBirthday().isAfter(LocalDate.now())) {
                return true;
            } else {
                throw new ValidationException("Не прошла валидация");
            }
        } catch (NullPointerException e) {
            throw new ValidationException("Пришел запрос с пустыми полями");
        }
    }
    public void addFriend(int id, int friendId) throws InvalidUserId {
        User user = findById(id);
        User friend = findById(friendId);
        storage.addFriend(id,friendId);
    }

    public Map<User,User> deleteFriend(int id, int friendId) throws InvalidUserId {
        User user = findById(id);
        User friend = findById(friendId);
        List<Integer> userFriends = storage.findFriends(id);
        if (userFriends.contains(friendId)) {
            storage.delete(id,friendId);
            return Map.of(storage.getUser(id),storage.getUser(friendId));
        } else {
            throw new InvalidUserId("Такого пользователя нет в друзьях");
        }
    }

    public Set<User> mutualFriends(int id, int friendId) throws InvalidUserId {
        User user = storage.getUser(id);
        User friend = storage.getUser(friendId);
        Set<User> userFriends = new HashSet(findFriends(id));
        Set<User> friendFriends = new HashSet(findFriends(friendId));
        return findCommonElements(userFriends,friendFriends);

    }
    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        Set<T> common = new HashSet<>(first);
        common.retainAll(second);
        return common;
    }

    public List<User> findFriends(int id) throws InvalidUserId {
        List<Integer> friends_id = storage.findFriends(id);
        List<User> friends = new ArrayList<>();
        for (int i: friends_id) {
            friends.add(storage.getUser(i));
        }
        return friends;
    }
}
