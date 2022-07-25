package com.example.finalproject.web.controllers;

import com.example.finalproject.persistence.model.Orders;
import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.service.implementation.OrderServiceImplementation;
import com.example.finalproject.web.DTO.CheckoutDTO;
import com.example.finalproject.web.DTO.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImplementation orderServiceImplementation;

    @GetMapping(value = "/API/users/orders")
    public List<OrderDTO> getAllOrders()
    {
        return orderServiceImplementation.getAllOrders();
    }

    @GetMapping(value = "/API/users/orders/{id}")
    public OrderDTO getOrder(@PathVariable long id)
    {
        return orderServiceImplementation.getOrder(id);
    }
}
