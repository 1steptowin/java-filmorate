package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
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
    public void addFriend(User user1, User user2) throws InvalidUserId {
        Set<Integer> friends1;
        Set<Integer> friends2;
        if (user1.getFriends() != null) {
            friends1 = user1.getFriends();
        } else {
            friends1 = new HashSet<>();
        }

        if (user2.getFriends() != null) {
            friends2 = user2.getFriends();
        } else {
            friends2 = new HashSet<>();
        }
        friends1.add(user2.getId());
        friends2.add(user1.getId());
        user1.setFriends(friends1);
        user2.setFriends(friends2);
        storage.update(user1);
        storage.update(user2);
    }

    public void deleteFriend(User user1, User user2) throws InvalidUserId {
        if (user1.getFriends() != null
        & user2.getFriends() != null) {
            if (user1.getFriends().contains(user2.getId())
                    & user2.getFriends().contains(user1.getId())) {
                Set<Integer> friends1 = user1.getFriends();
                friends1.remove(user2.getId());
                Set<Integer> friends2 = user2.getFriends();
                friends2.remove(user1.getId());
                storage.update(user1);
                storage.update(user2);
            }
        }
         else {
            throw new InvalidUserId("Некорректный id пользователя");
        }

    }

    public Set<User> mutualFriends(User user1, User user2) {
        try {
            Set<Integer> common = findCommonElements(user1.getFriends(),user2.getFriends());
            Set<User> commonUsers = new HashSet<>();
            for (Integer i: common) {
                commonUsers.add(storage.getUser(i));
            }
            return commonUsers;
        } catch (NullPointerException e) {
            return new HashSet<>();
        }

    }
    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        Set<T> common = new HashSet<>(first);
        common.retainAll(second);
        return common;
    }

}
