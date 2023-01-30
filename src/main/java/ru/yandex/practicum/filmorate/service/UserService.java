package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.InvalidFilmId;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(User user1, User user2) throws InvalidFilmId, InvalidUserId {
        Set<Integer> friends1 = user1.getFriends();
        friends1.add(user2.getId());
        Set<Integer> friends2 = user2.getFriends();
        friends2.add(user1.getId());
        storage.update(user1);
        storage.update(user2);
    }

    public void deleteFriend(User user1, User user2) throws InvalidFilmId, InvalidUserId {
        Set<Integer> friends1 = user1.getFriends();
        friends1.remove(user2.getId());
        Set<Integer> friends2 = user2.getFriends();
        friends2.remove(user1.getId());
        storage.update(user1);
        storage.update(user2);
    }

    public Set<Integer> mutualFriends(User user1, User user2) {

        return findCommonElements(user1.getFriends(),user2.getFriends());
    }
    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        Set<T> common = new HashSet<>(first);
        common.retainAll(second);
        return common;
    }

}
