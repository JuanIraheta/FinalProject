package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.ProductServiceImplementation;
import com.example.finalproject.web.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final ProductServiceImplementation productServiceImplementation;

    @GetMapping(value = "/API/products")
    public List<ProductDTO> getProducts()
    {
        return productServiceImplementation.getAllProducts();
    }
    @GetMapping(value = "/API/products/{id}")
    public ProductDTO getProduct(@PathVariable long id)
    {
        return productServiceImplementation.getProduct(id);
    }

}
