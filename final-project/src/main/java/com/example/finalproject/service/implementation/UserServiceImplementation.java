package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.UserService;
import com.example.finalproject.service.mapper.UserMapper;
import com.example.finalproject.web.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

//    @Override
//    public List<UserDTO> getAllUsers() {
//
//        List<User> users = userRepository.findAll();
//        return UserMapper.INSTANCE.usersToUserDTOS(users);
//    }

    @Override
    public UserDTO getUser(String userEmail) {
        User foundUser = foundUser(userEmail);
        return UserMapper.INSTANCE.userToUserDTO(foundUser);
    }

    private User foundUser (String email)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user;
    }
}
