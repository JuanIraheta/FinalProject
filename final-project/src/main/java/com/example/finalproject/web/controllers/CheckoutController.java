package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.web.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutServiceImplementation checkoutServiceImplementation;


    @GetMapping(value = "/API/users/checkouts")
    public CheckoutDTO getCheckout(@AuthenticationPrincipal Jwt principal)
    {
        return checkoutServiceImplementation.getCheckout(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/API/users/checkouts")
    public ResponseEntity<String> createCheckout (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateCheckoutDTO checkoutDTO)
    {
        checkoutServiceImplementation.createCheckOut(getEmailByPrincipal(principal),checkoutDTO);
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.OK);
    }

    @PostMapping(value = "/API/users/checkouts/products")
    public ResponseEntity<String> addProductToCheckout (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.addProductToCheckout(getEmailByPrincipal(principal),checkoutProductDTO);
        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/API/users/checkouts/products/{id}")
    public ResponseEntity<String> modifyCheckoutProductQuantity (@AuthenticationPrincipal Jwt principal,@PathVariable long id, @RequestBody @Valid UpdateCheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.modifyCheckoutProductQuantity(getEmailByPrincipal(principal),id,checkoutProductDTO);
        return new ResponseEntity<>("Product quantity modified successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/API/users/checkouts/products/{id}")
    public ResponseEntity<String> deleteCheckoutProduct(@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.deleteCheckoutProduct(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Product Deleted Successfully",HttpStatus.OK);
    }

    @DeleteMapping(value = "/API/users/checkouts")
    public ResponseEntity<String> deleteCheckout(@AuthenticationPrincipal Jwt principal)
    {
        checkoutServiceImplementation.deleteCheckout(getEmailByPrincipal(principal));
        return new ResponseEntity<>("Checkout Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping(value = "API/users/checkouts/addresses/{id}")
    public ResponseEntity<String> changeCheckoutAddress (@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.changeCheckoutAddress(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Address Changed Successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/API/users/checkouts/addresses")
    public List<CheckoutUserAddressDTO> getAllUserAddresses(@AuthenticationPrincipal Jwt principal)
    {
        return checkoutServiceImplementation.getAllAddresses(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/API/users/checkouts/addresses")
    public ResponseEntity<String> createAddress (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateAddressDTO createAddressDTO)
    {
        checkoutServiceImplementation.createAddress(getEmailByPrincipal(principal),createAddressDTO);
        return new ResponseEntity<>("Address Added Successfully", HttpStatus.OK);
    }

    @PutMapping(value = "API/users/checkouts/payments/{id}")
    public ResponseEntity<String> changeCheckoutPaymentMethod (@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.changeCheckoutPaymentMethod(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Payment Method Changed Successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/API/users/checkouts/payments")
    public List<PaymentMethodDTO> getAllUserPaymentMethods(@AuthenticationPrincipal Jwt principal)
    {
        return checkoutServiceImplementation.getAllPaymentMethods(getEmailByPrincipal(principal));
    }

    @PostMapping(value = "/API/users/checkouts/payments")
    public ResponseEntity<String> createPaymentMethod (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        checkoutServiceImplementation.createPaymentMethod(getEmailByPrincipal(principal),createPaymentMethodDTO);
        return new ResponseEntity<>("Payment Method Added Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/API/users/checkouts/purchases")
    public ResponseEntity<String> generateOrder (@AuthenticationPrincipal Jwt principal)
    {
        checkoutServiceImplementation.generateOrder(getEmailByPrincipal(principal));
        return new ResponseEntity<>("Order Successfully Generated", HttpStatus.OK);
    }

    private String getEmailByPrincipal (Jwt principal)
    {
        return principal.getClaims().get("email").toString();
    }

}
