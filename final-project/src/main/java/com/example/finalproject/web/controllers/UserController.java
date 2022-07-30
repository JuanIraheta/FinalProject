package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.UserServiceImplementation;
import com.example.finalproject.web.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUsers(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getUser(getEmailByPrincipal(principal));
    }

//    @GetMapping(value = "/test")
//    public ResponseEntity<String> test (@AuthenticationPrincipal Jwt principal)
//    {
//        if (principal == null)
//        {
//            return new ResponseEntity<>("Authenticate", HttpStatus.UNAUTHORIZED);
//        }
//        String email = principal.getClaims().get("email").toString();
//        return new ResponseEntity<>(email, HttpStatus.OK);
//    }

    @GetMapping(value = "/API/users/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<UserAddressDTO> getAllUserAddresses(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getAllAddresses(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/API/users/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createAddress (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateAddressDTO createAddressDTO)
    {
        userServiceImplementation.createAddress(getEmailByPrincipal(principal),createAddressDTO);
        return new ResponseEntity<>("Address Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping(value = "/API/users/payments")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentMethodDTO> getAllUserPaymentMethods(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getAllPaymentMethods(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/API/users/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createPaymentMethod (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        userServiceImplementation.createPaymentMethod(getEmailByPrincipal(principal),createPaymentMethodDTO);
        return new ResponseEntity<>("Payment Method Added Successfully", HttpStatus.CREATED);
    }

    private String getEmailByPrincipal (Jwt principal)
    {
        return principal.getClaims().get("email").toString();
    }



}
