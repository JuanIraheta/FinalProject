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
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUsers(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getUser(getEmailByPrincipal(principal));
    }


    @GetMapping(value = "/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<UserAddressDTO> getAllUserAddresses(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getAllAddresses(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createAddress (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateAddressDTO createAddressDTO)
    {
        userServiceImplementation.createAddress(getEmailByPrincipal(principal),createAddressDTO);
        return new ResponseEntity<>("Address Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping(value = "/payments")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentMethodDTO> getAllUserPaymentMethods(@AuthenticationPrincipal Jwt principal)
    {
        return userServiceImplementation.getAllPaymentMethods(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/payments")
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
