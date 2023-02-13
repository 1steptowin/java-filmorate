package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public ArrayList<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(users.size()+1);
        users.put(users.size() + 1, user);
    }

    @Override
    public void update(User user) throws  InvalidUserId {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new InvalidUserId("Пользователя не существует");
        }
    }

    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public void addFriend(int id, int friendId) {

    }

    @Override
    public List<Integer> findFriends(int id) {
        return null;
    }

}
