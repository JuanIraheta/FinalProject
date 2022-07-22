package com.example.finalproject.service;

import com.example.finalproject.persistence.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(long id);
}
