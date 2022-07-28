package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.UserServiceImplementation;
import com.example.finalproject.web.DTO.CheckoutProductDTO;
import com.example.finalproject.web.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

//    @GetMapping(value = "/API/users")
//    public List<UserDTO> getUsers()
//    {
//        return userServiceImplementation.getAllUsers();
//    }

    @GetMapping(value = "/API/users")
    public UserDTO getUsers(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getUser(getEmailByPrincipal(principal));
    }

    @GetMapping(value = "/test")
    public ResponseEntity<String> test (@AuthenticationPrincipal Jwt principal)
    {
        if (principal == null)
        {
            return new ResponseEntity<>("Authenticate", HttpStatus.UNAUTHORIZED);
        }
        String email = principal.getClaims().get("email").toString();
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    private String getEmailByPrincipal (Jwt principal)
    {
        return principal.getClaims().get("email").toString();
    }



}
