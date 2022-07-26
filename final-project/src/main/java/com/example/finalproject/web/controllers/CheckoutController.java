package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.web.DTO.*;
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
    public CheckoutDTO getCheckout()
    {
        return checkoutServiceImplementation.getCheckout();
    }

    @PostMapping(value = "/API/users/checkouts")
    public ResponseEntity<String> createCheckout (@RequestBody @Valid CreateCheckoutDTO checkoutDTO)
    {
        checkoutServiceImplementation.createCheckOut(checkoutDTO);
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
    }

    @PostMapping(value = "/API/users/checkouts/products")
    public ResponseEntity<String> addProductToCheckout (@RequestBody @Valid CheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.addProductToCheckout(checkoutProductDTO);
        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/API/users/checkouts/products/{id}")
    public ResponseEntity<String> modifyCheckoutProductQuantity (@PathVariable String id, @RequestBody @Valid UpdateCheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.modifyCheckoutProductQuantity(id,checkoutProductDTO);
        return new ResponseEntity<>("Product quantity modified successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/API/users/checkouts/products/{id}")
    public ResponseEntity<String> deleteCheckoutProduct(@PathVariable String id)
    {
        checkoutServiceImplementation.deleteCheckoutProduct(id);
        return new ResponseEntity<>("Product Deleted Successfully",HttpStatus.OK);
    }

    @DeleteMapping(value = "/API/users/checkouts")
    public ResponseEntity<String> deleteCheckout()
    {
        checkoutServiceImplementation.deleteCheckout();
        return new ResponseEntity<>("Checkout Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping(value = "API/users/checkouts/addresses")
    public ResponseEntity<String> changeCheckoutAddress (@RequestParam long addressID)
    {
        checkoutServiceImplementation.changeCheckoutAddress(addressID);
        return new ResponseEntity<>("Address Changed Successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/API/users/checkouts/addresses")
    public List<CheckoutUserAddressDTO> getAllUserAddresses()
    {
        return checkoutServiceImplementation.getAllAddresses();
    }

    @PostMapping(value = "/API/users/checkouts/addresses")
    public ResponseEntity<String> createAddress (@RequestBody @Valid CreateAddressDTO createAddressDTO)
    {
        checkoutServiceImplementation.createAddress(createAddressDTO);
        return new ResponseEntity<>("Address Added Successfully", HttpStatus.OK);
    }

    @PutMapping(value = "API/users/checkouts/payments")
    public ResponseEntity<String> changeCheckoutPaymentMethod (@RequestParam long paymentID)
    {
        checkoutServiceImplementation.changeCheckoutPaymentMethod(paymentID);
        return new ResponseEntity<>("Payment Method Changed Successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/API/users/checkouts/payments")
    public List<PaymentMethodDTO> getAllUserPaymentMethods()
    {
        return checkoutServiceImplementation.getAllPaymentMethods();
    }

    @PostMapping(value = "/API/users/checkouts/payments")
    public ResponseEntity<String> createPaymentMethod (@RequestBody @Valid CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        checkoutServiceImplementation.createPaymentMethod(createPaymentMethodDTO);
        return new ResponseEntity<>("Payment Method Added Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/API/users/checkouts/purchases")
    public ResponseEntity<String> generateOrder ()
    {
        checkoutServiceImplementation.generateOrder();
        return new ResponseEntity<>("Order Successfully Generated", HttpStatus.OK);
    }

}
