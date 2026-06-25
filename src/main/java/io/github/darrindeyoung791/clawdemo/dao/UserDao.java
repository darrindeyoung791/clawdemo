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

    public void insert(User user) {
        jdbc.update("INSERT INTO user (id, username, password, display_name, role) VALUES (?, ?, ?, ?, ?)",
                user.getId(), user.getUsername(), user.getPassword(), user.getDisplayName(), user.getRole());
    }
}
