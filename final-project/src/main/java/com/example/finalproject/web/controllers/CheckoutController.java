package com.example.finalproject.web.controllers;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutServiceImplementation checkoutServiceImplementation;


    @GetMapping(value = "/API/users/checkouts")
    public List<Checkout> getCheckouts()
    {
        return checkoutServiceImplementation.getAllCheckouts();
    }

    @PostMapping(value = "/API/users/checkout")
    public ResponseEntity<String> createCheckout ()
    {
        checkoutServiceImplementation.createCheckOut();
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
    }
    @PostMapping(value = "/API/users/checkout/product")
    public ResponseEntity<String> createCheckoutProduct ()
    {
        checkoutServiceImplementation.CreateCheckoutProduct();
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
    }

//    @PostMapping(value = "/API/users/checkout")
//    public ResponseEntity<String> createCheckout (@RequestBody @Valid CreateCheckoutDTO checkoutDTO)
//    {
//        checkoutServiceImplementation.createCheckOut(checkoutDTO);
//        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
//    }
}
