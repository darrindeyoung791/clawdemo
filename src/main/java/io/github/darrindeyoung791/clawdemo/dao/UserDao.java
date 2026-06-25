package io.github.darrindeyoung791.clawdemo.dao;

import io.github.darrindeyoung791.clawdemo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbc;

    public User findByUsername(String username) {
        List<User> list = jdbc.query("SELECT * FROM user WHERE username = ?",
                new BeanPropertyRowMapper<>(User.class), username);
        return list.isEmpty() ? null : list.get(0);
    }

    public User findByUsernameOrDisplayName(String name) {
        List<User> list = jdbc.query("SELECT * FROM user WHERE username = ? OR display_name = ?",
                new BeanPropertyRowMapper<>(User.class), name, name);
        return list.isEmpty() ? null : list.get(0);
    }

    public int countByUsernameOrDisplayName(String username, String displayName) {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM user WHERE username IN (?, ?) OR display_name IN (?, ?)",
            Integer.class, username, displayName, username, displayName);
    }

    public void insert(User user) {
        jdbc.update("INSERT INTO user (id, username, password, display_name, role) VALUES (?, ?, ?, ?, ?)",
                user.getId(), user.getUsername(), user.getPassword(), user.getDisplayName(), user.getRole());
    }
}
