package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.UserServiceImplementation;
import com.example.finalproject.web.DTO.UserDTO;
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
    public List<UserDTO> getUsers()
    {
        return userServiceImplementation.getAllUsers();
    }

    @GetMapping(value = "/API/users/{id}")
    public UserDTO getUsers(@PathVariable Long id)
    {
        return userServiceImplementation.getUser(id);
    }

}
