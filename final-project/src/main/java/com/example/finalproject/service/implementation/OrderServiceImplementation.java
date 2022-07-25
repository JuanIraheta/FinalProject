package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.Orders;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.OrderProductRepository;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImplementation {

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final UserRepository userRepository;

    public List<Orders> getAllOrders()
    {
        return orderRepository.findAll();
    }







    private User getUser (long id)
    {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user.get();
    }
}
