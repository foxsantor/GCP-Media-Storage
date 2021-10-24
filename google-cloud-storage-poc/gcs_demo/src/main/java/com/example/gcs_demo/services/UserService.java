package com.example.gcs_demo.services;

import com.example.gcs_demo.models.User;

import java.util.List;

public interface UserService {

    User insertUser(User user);

    User deleteUser(String id);

    List<User> getAllUsers();

    User getUserData(String id);

    User updateUser(User id);

}
