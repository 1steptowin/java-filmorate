package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
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
    public User update(User user) throws  InvalidUserId {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new InvalidUserId("Пользователя не существует");
        }
    }

    public User getUser(int id) {
        return users.get(id);
    }

}
