package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.OrderServiceImplementation;
import com.example.finalproject.web.DTO.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/orders")
public class OrderController {

    private final OrderServiceImplementation orderServiceImplementation;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders(@AuthenticationPrincipal Jwt principal)
    {
        return orderServiceImplementation.getAllOrders(getEmailByPrincipal(principal));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrder(@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        return orderServiceImplementation.getOrder(getEmailByPrincipal(principal),id);
    }

    private String getEmailByPrincipal (Jwt principal)
    {
        return principal.getClaims().get("email").toString();
    }
}
