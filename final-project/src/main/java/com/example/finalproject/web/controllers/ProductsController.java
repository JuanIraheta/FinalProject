package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.ProductServiceImplementation;
import com.example.finalproject.web.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductsController {

    private final ProductServiceImplementation productServiceImplementation;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProducts()
    {
        return productServiceImplementation.getAllProducts();
    }
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable long id)
    {
        return productServiceImplementation.getProduct(id);
    }

}
