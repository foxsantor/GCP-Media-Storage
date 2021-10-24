package com.example.gcs_demo.services;

import com.example.gcs_demo.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final List<User> users = new ArrayList<>();

    @Override
    public User insertUser(User user) {
        users.add(user);
        log.info("a user has been added");
        return user;
    }

    @Override
    public User deleteUser(String id) {
        User userToDelete = users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
        users.remove(userToDelete);
        log.info("a user has been deleted : [{}]", users);
        return userToDelete;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserData(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
    }

    @Override
    public User updateUser(User user) {
        User newUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().get();
        users.remove(newUser);
        users.add(user);
        return null;
    }
}
