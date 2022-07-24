package com.example.finalproject.web.controllers;

import com.example.finalproject.persistence.model.User;
import com.example.finalproject.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    @GetMapping(value = "/API/users")
    public List<User> getUsers()
    {
        return userServiceImplementation.getAllUsers();
    }

    @GetMapping(value = "/API/users/{id}")
    public User getUsers(@PathVariable Long id)
    {
        return userServiceImplementation.getUser(id);
    }

}
