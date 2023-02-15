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
import ru.yandex.practicum.filmorate.storage.mapper.FriendssMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ArrayList<User> findAll() throws InvalidUserId {
        String sql = "SELECT * FROM users";
        ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(sql,new UserMapper());
        ArrayList<User> usersNew = new ArrayList<>();
        for (User user: users) {
            usersNew.add(getUser(user.getId()));
        }
        return usersNew;
    }


    @Override
    public User create(User user) throws SqlUpdateException {
        String sql = "INSERT INTO users (email,login,name, birthday) " +
                "values (?,?,?,?)";
        try {
            jdbcTemplate.update(sql,user.getEmail(),user.getLogin(),user.getName(),user.getBirthday());
            log.info("Создан юзер " + user.getName());
            return getUser(getIdByLogin(user.getLogin()));
        } catch (Exception e) {
            throw new SqlUpdateException("Ошибка создания пользователя");
        }
    }

    @Override
    public User update(User user) throws InvalidUserId, SqlUpdateException {
        String sql = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE ID = ?";

        try {
            jdbcTemplate.update(sql,user.getName(),user.getLogin(),user.getEmail(),user.getBirthday(),user.getId());
            log.info("Обновлен юзер " + user.getId());
            return getUser(user.getId());
        } catch (Exception e) {
            log.warn("Ошибка при обновлении пользователя");
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
            try {
                List<HashMap<Integer,Boolean>> map = jdbcTemplate.query("SELECT friend_id,status FROM FRIENDSHIP where user_id = ?",new FriendssMapper(), user.getId());
                HashMap<Integer,Boolean> friends = new HashMap<>();
                for (HashMap<Integer,Boolean> i: map) {
                    for (Integer inter: i.keySet()) {
                        friends.put(inter,i.get(inter));
                    }
                }
                user.setFriends(friends);
            } catch (Exception e ) {
                HashMap<Integer,Boolean> newMap = new HashMap<>();
                user.setFriends(newMap);
            }
            return user;
        } else
            throw new InvalidUserId("Такого пользователя нет");
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO friendship (user_id, friend_id, status)" +
                "values(?,?,?)";
        try {
            jdbcTemplate.update(sql,id,friendId,true);
            jdbcTemplate.update(sql,friendId,id,false);
            log.info("Друзья добавлены: "+ id + " " + friendId);
        } catch (Exception e) {
            log.warn("Уже есть строка");
        }
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
        log.info("Удален друг у " + id + " " + friendId);
    }

    private int getIdByLogin (String login) throws SqlUpdateException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT id FROM users WHERE login = ?",login);
        if (rowSet.next()) {
            return rowSet.getInt("id");
        } else throw new SqlUpdateException("Проблема в запросе");
    }
    private ArrayList<HashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}
