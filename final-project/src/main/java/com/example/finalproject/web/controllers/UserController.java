package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.UserServiceImplementation;
import com.example.finalproject.web.DTO.CheckoutProductDTO;
import com.example.finalproject.web.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping(value = "/test")
    public ResponseEntity<String> tets ()
    {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }



}
