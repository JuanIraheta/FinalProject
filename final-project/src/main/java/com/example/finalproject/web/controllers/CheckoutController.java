package com.example.finalproject.web.controllers;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.web.DTO.CheckoutProductDTO;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import com.example.finalproject.web.DTO.UpdateCheckoutProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/API/users/checkouts")
    public ResponseEntity<String> createCheckout (@RequestBody @Valid CreateCheckoutDTO checkoutDTO)
    {
        checkoutServiceImplementation.createCheckOut(checkoutDTO);
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
    }

    @PostMapping(value = "/API/user/checkouts/products")
    public ResponseEntity<String> addProductToCheckout (@RequestBody @Valid CheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.addProductToCheckout(checkoutProductDTO);
        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/API/user/checkout/products/{id}")
    public ResponseEntity<String> modifyCheckoutProductQuantity (@PathVariable String id, @RequestBody @Valid UpdateCheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.modifyProductQuantity(id,checkoutProductDTO);
        return new ResponseEntity<>("Product modified successfully", HttpStatus.OK);
    }
}
