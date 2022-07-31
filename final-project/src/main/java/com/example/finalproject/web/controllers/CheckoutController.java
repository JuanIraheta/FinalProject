package com.example.finalproject.web.controllers;

import com.example.finalproject.service.implementation.CheckoutServiceImplementation;
import com.example.finalproject.web.DTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/checkouts")
public class CheckoutController {

    private final CheckoutServiceImplementation checkoutServiceImplementation;


    @Operation(summary = "Used to get the checkout that is related to the current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Obtained the checkout information of the current user",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Could not find a checkout related to the current user",
                    content = @Content)
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CheckoutDTO getCheckout(@AuthenticationPrincipal Jwt principal)
    {
        return checkoutServiceImplementation.getCheckout(getEmailByPrincipal(principal));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createCheckout (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateCheckoutDTO checkoutDTO)
    {
        checkoutServiceImplementation.createCheckOut(getEmailByPrincipal(principal),checkoutDTO);
        return new ResponseEntity<>("Checkout successfully created", HttpStatus.CREATED);
    }

    @PostMapping(value = "/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addProductToCheckout (@AuthenticationPrincipal Jwt principal,@RequestBody @Valid CreateCheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.addProductToCheckout(getEmailByPrincipal(principal),checkoutProductDTO);
        return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
    }

    @PutMapping(value = "/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> modifyCheckoutProductQuantity (@AuthenticationPrincipal Jwt principal,@PathVariable long id, @RequestBody @Valid UpdateCheckoutProductDTO checkoutProductDTO)
    {
        checkoutServiceImplementation.modifyCheckoutProductQuantity(getEmailByPrincipal(principal),id,checkoutProductDTO);
        return new ResponseEntity<>("Product quantity modified successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteCheckoutProduct(@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.deleteCheckoutProduct(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Product Deleted Successfully",HttpStatus.OK);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteCheckout(@AuthenticationPrincipal Jwt principal)
    {
        checkoutServiceImplementation.deleteCheckout(getEmailByPrincipal(principal));
        return new ResponseEntity<>("Checkout Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping(value = "/addresses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> changeCheckoutAddress (@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.changeCheckoutAddress(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Address Changed Successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/payments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> changeCheckoutPaymentMethod (@AuthenticationPrincipal Jwt principal,@PathVariable long id)
    {
        checkoutServiceImplementation.changeCheckoutPaymentMethod(getEmailByPrincipal(principal),id);
        return new ResponseEntity<>("Payment Method Changed Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/purchases")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> generateOrder (@AuthenticationPrincipal Jwt principal)
    {
        checkoutServiceImplementation.generateOrder(getEmailByPrincipal(principal));
        return new ResponseEntity<>("Order Successfully Generated", HttpStatus.CREATED);
    }

    private String getEmailByPrincipal (Jwt principal)
    {
        return principal.getClaims().get("email").toString();
    }

}
