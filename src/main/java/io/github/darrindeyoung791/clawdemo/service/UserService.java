package io.github.darrindeyoung791.clawdemo.service;

import io.github.darrindeyoung791.clawdemo.dao.BorrowRecordDao;
import io.github.darrindeyoung791.clawdemo.dao.UserDao;
import io.github.darrindeyoung791.clawdemo.entity.User;
import io.github.darrindeyoung791.clawdemo.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final BorrowRecordDao borrowRecordDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(String username, String password, String displayName) {
        if (userDao.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userDao.findByDisplayName(displayName) != null) {
            throw new RuntimeException("Display name already exists");
        }
        if (userDao.findByUsername(displayName) != null) {
            throw new RuntimeException("Display name conflicts with existing username");
        }
        if (userDao.findByDisplayName(username) != null) {
            throw new RuntimeException("Username conflicts with existing display name");
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setDisplayName(displayName);
        user.setRole("ROLE_USER");
        userDao.insert(user);
    }

    public List<User> listAll() {
        return userDao.findAll();
    }

    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        if (newPassword.length() < 6 || newPassword.length() > 18) {
            throw new RuntimeException("New password must be 6-18 characters");
        }
        userDao.updatePassword(userId, passwordEncoder.encode(newPassword));
    }

    public void deleteUser(String id) {
        borrowRecordDao.deleteByUserId(id);
        userDao.deleteById(id);
    }

    @PostConstruct
    public void initAdmin() {
        if (userDao.countByUsernameOrDisplayName("root1", "root1") == 0) {
            User admin = new User();
            admin.setId(UUID.randomUUID().toString());
            admin.setUsername("root1");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setDisplayName("root1");
            admin.setRole("ROLE_ADMIN");
            userDao.insert(admin);
        }
    }

    public Map<String, Object> login(String username, String password) {
        User user = userDao.findByUsernameOrDisplayName(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("displayName", user.getDisplayName());
        result.put("role", user.getRole());
        return result;
    }
}
