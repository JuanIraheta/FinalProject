package com.example.finalproject.service.implementation;

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

    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user: users)
        {
            UserDTO dto = UserMapper.INSTANCE.userToUserDTO(user);
            userDTOS.add(dto);
        }

        return userDTOS;
    }

    @Override
    public UserDTO getUser(long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return UserMapper.INSTANCE.userToUserDTO(foundUser.get());
    }
}
