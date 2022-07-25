package com.example.finalproject.service;

import com.example.finalproject.persistence.model.User;
import com.example.finalproject.web.DTO.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO>getAllUsers();

    UserDTO getUser(long id);
}
