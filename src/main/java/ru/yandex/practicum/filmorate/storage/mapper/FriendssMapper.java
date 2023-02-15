package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FriendssMapper implements RowMapper<HashMap<Integer,Boolean>> {
    @Override
    public HashMap<Integer,Boolean> mapRow(ResultSet rs, int rowNum) throws SQLException {
        HashMap<Integer,Boolean> map = new HashMap<>();
        map.put(rs.getInt("friend_id"),rs.getBoolean("status"));
        return map;
    }
}
