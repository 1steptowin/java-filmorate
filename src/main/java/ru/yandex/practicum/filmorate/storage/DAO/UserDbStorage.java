package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.InvalidUserId;
import ru.yandex.practicum.filmorate.Exception.SqlUpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FriendsMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("UserDBStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<User> findAll() {
        String sql = "SELECT * FROM users";
        return (ArrayList<User>) jdbcTemplate.query(sql,new UserMapper());
    }


    @Override
    public User create(User user) throws SqlUpdateException {
        String sql = "INSERT INTO users (email,login,name, birthday) " +
                "values (?,?,?,?)";
        try {
            jdbcTemplate.update(sql,user.getEmail(),user.getLogin(),user.getName(),user.getBirthday());
            return getUser(getIdByLogin(user.getLogin()));
        } catch (Exception e) {
            throw new SqlUpdateException("Пользователь не существует");
        }
    }

    @Override
    public User update(User user) throws InvalidUserId, SqlUpdateException {
        String sql = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE ID = ?";

        try {
            jdbcTemplate.update(sql,user.getName(),user.getLogin(),user.getEmail(),user.getBirthday(),user.getId());
            return getUser(user.getId());
        } catch (Exception e) {
            throw new InvalidUserId("Пользователя не существует");
        }
    }

    @Override
    public User getUser(int id) throws InvalidUserId {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("Select * FROM users WHERE id = ?",id);
        if (userRows.next()) {
            User user = new User();
            user.setId(userRows.getInt("id"));
            user.setName(userRows.getString("name"));
            user.setLogin(userRows.getString("login"));
            user.setEmail(userRows.getString("email"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
            return user;
        } else
            throw new InvalidUserId("Такого пользователя нет");
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO friendship (user_id, friend_id, status)" +
                "values(?,?,?)";
        jdbcTemplate.update(sql,id,friendId,true);
        jdbcTemplate.update(sql,friendId,id,false);
    }
    @Override
    public List<Integer> findFriends(int id) throws InvalidUserId {
        try {
            String sql = "SELECT friend_id FROM friendship WHERE user_id = ? AND status = true";
             return jdbcTemplate.query(sql,new FriendsMapper(),id);
        } catch (Exception e) {
            throw new InvalidUserId("Такого пользователя нет");
        }
    }
    @Override
    public void delete(int id, int friendId) {
        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql,id,friendId);
    }

    private int getIdByLogin (String login) throws SqlUpdateException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT id FROM users WHERE login = ?",login);
        if (rowSet.next()) {
            return rowSet.getInt("id");
        } else throw new SqlUpdateException("Проблема в запросе");
    }
}
