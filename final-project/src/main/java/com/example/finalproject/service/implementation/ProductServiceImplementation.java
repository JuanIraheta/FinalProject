package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.Product;
import com.example.finalproject.persistence.repository.ProductRepository;
import com.example.finalproject.service.ProductService;
import com.example.finalproject.service.mapper.ProductsMapper;
import com.example.finalproject.web.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return ProductsMapper.INSTANCE.productsToProductDTOS(products);
    }

    @Override
    public ProductDTO getProduct(String name) {
        return ProductsMapper.INSTANCE.productToProductDTO(productRepository.findByName(name));
    }
}
