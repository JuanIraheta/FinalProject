package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.*;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import org.h2.command.ddl.CreateUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplementationTest {
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  UserRepository userRepository;

    private OrderServiceImplementation orderServiceImplementation;

    @BeforeEach
    private void setUp ()
    {
        orderServiceImplementation = new OrderServiceImplementation(orderRepository,userRepository);
    }

    @Test
    void getOrder_ValidEmail_GetSpecificOrder()
    {
        User user = createUser();
        Orders order = createOrder();

        when(orderRepository.findByUserAndId(any(),anyLong())).thenReturn(order);
        when(userRepository.findByEmail(any())).thenReturn(user);

        orderServiceImplementation.getOrder("irahetajuanjose@hotmail.com",1L);

        verify(orderRepository).findByUserAndId(any(),anyLong());
        verify(userRepository).findByEmail(any());

    }

    private User createUser ()
    {
        return User.builder()
                .id(1L)
                .email("irahetajuanjose@hotmail.com")
                .userName("loading")
                .firstName("Juan")
                .lastName("Jose")
                .phoneNumber("+503 7129 9991")
                .build();

    }

    private Orders createOrder (){
        return Orders.builder()
                .id(1l)
                .user(createUser())
                .orderProducts(createOrderProducts())
                .build();
    }

    private List<OrderProduct> createOrderProducts()
    {
        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .product(createProduct())
                .quantity(2)
                .build();
        orderProducts.add(orderProduct);
        return orderProducts;
    }

    private Product createProduct ()
    {
        return Product.builder()
                .id(1L)
                .name("water")
                .price(5.00)
                .stock(3)
                .build();
    }

    private PaymentMethod createPaymentMethod ()
    {
        return PaymentMethod.builder()
                .id(1L)
                .name("payment")
                .user(createUser())
                .founds(50.00)
                .build();
    }

}