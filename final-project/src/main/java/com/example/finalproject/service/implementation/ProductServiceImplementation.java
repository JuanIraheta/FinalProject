package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty())
        {
            throw new ResourceNotFoundException("There are no products available");
        }

        return ProductsMapper.INSTANCE.productsToProductDTOS(products);
    }

    @Override
    public ProductDTO getProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
        {
            throw new ResourceNotFoundException("There is no product with this id");
        }
        return ProductsMapper.INSTANCE.productToProductDTO(product.get());
    }
}
