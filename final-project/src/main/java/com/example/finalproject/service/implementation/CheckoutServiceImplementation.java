package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.persistence.model.CheckoutProduct;
import com.example.finalproject.persistence.model.Product;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.CheckoutProductRepository;
import com.example.finalproject.persistence.repository.CheckoutRepository;
import com.example.finalproject.persistence.repository.ProductRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.CheckoutService;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImplementation implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final CheckoutProductRepository checkoutProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }

    public void createCheckOut()
    {
        //Crear el checkout basico
        User getUser = userRepository.findById(1l).get();
        Product getProduct = productRepository.findById(1l).get();
        Checkout checkout = Checkout.builder()
                .user(getUser)
                .address(getUser.getAddress().get(0))
                .paymentMethod(getUser.getPaymentMethods().get(0))
                .build();

        Checkout savedCheckout = checkoutRepository.save(checkout);

        //Crear su respectivo checkout product
        CheckoutProduct checkoutProduct = new CheckoutProduct();
        checkoutProduct.setProduct(getProduct);
        checkoutProduct.setQuantity(1);
        checkoutProduct.setCheckout(savedCheckout);

        checkoutProductRepository.save(checkoutProduct);

    }

    public void CreateCheckoutProduct()
    {
        CheckoutProduct checkoutProduct = new CheckoutProduct();
        Product getProduct = productRepository.findById(1l).get();
        checkoutProduct.setProduct(getProduct);
        checkoutProduct.setQuantity(1);

        checkoutProductRepository.save(checkoutProduct);
    }
}
