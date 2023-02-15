package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    @Autowired
    private final UserStorage userStorage;

    public static User createUser() {
        User user = new User();
        user.setEmail("email@123");
        user.setId(1);
        user.setName("1");
        user.setLogin("2");
        HashMap<Integer,Boolean> list = new HashMap<>();
        user.setFriends(list);
        user.setBirthday(LocalDate.of(2022,01,01));
        return user;
    }
    @BeforeAll
    void createTestUser() throws SqlUpdateException {
        userStorage.create(createUser());
    }
    @AfterEach
    void updateUser() throws InvalidUserId, SqlUpdateException {
        userStorage.update(createUser());
    }
    @Test
    public void testGetUser() throws InvalidUserId {
        User user = createUser();
        Assert.assertThat(user,equalTo(userStorage.getUser(1)));
    }

    @Test
    public void testFindAll() throws InvalidUserId {
        User user = createUser();
        List<User> list = new ArrayList<>();
        list.add(user);
        Assert.assertThat(list,equalTo(userStorage.findAll()));
    }

    @Test
    public void testUpdateUser() throws InvalidUserId, SqlUpdateException {
        User user = createUser();
        user.setLogin("testUpdateUser");
        userStorage.update(user);
        Assert.assertThat(user,equalTo(userStorage.getUser(1)));
    }

    @Test
    public void testAddFriend() throws InvalidUserId {
        HashMap<Integer,Boolean> list = new HashMap<>();
        list.put(2,true);
        User user = userStorage.getUser(1);
        userStorage.addFriend(1,2);
        Assert.assertThat(list,equalTo(user.getFriends()));
    }

    @Test
    public void testFindFriends() throws InvalidUserId {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        userStorage.addFriend(1,2);
        Assert.assertThat(list,equalTo(userStorage.findFriends(1)));
    }

}