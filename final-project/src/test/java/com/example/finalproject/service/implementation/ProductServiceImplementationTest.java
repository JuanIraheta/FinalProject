package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.Product;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImplementation productServiceImplementation;

    @BeforeEach
    private void setUp ()
    {
        productServiceImplementation = new ProductServiceImplementation(productRepository);
    }

//    @Nested
//    @DisplayName("getProduct")
//    class getProduct {
//        @Test
//        @DisplayName("getOrder When valid email return valid OrderDTO")
//        void getProduct_ValidUserEmail_GetOrderDTO()
//    }

}