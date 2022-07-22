package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.get();
    }
}
