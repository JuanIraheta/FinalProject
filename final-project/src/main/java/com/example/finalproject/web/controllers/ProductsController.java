package com.example.finalproject.web.controllers;

import com.example.finalproject.persistence.model.Product;
import com.example.finalproject.service.implementation.ProductServiceImplementation;
import com.example.finalproject.web.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
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
    @GetMapping(value = "/API/products/{name}")
    public ProductDTO getProduct(@PathVariable String name)
    {
        return productServiceImplementation.getProduct(name);
    }

}
