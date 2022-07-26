package com.example.finalproject.service;

import com.example.finalproject.web.DTO.*;

import java.util.List;

public interface CheckoutService {

    CheckoutDTO getCheckout();
    void createCheckOut(CreateCheckoutDTO checkoutDTO);

    void addProductToCheckout(CheckoutProductDTO checkoutProductDTO);

    void modifyCheckoutProductQuantity(String productName, UpdateCheckoutProductDTO updateCheckoutProductDTO);

    void deleteCheckoutProduct(String productName);

    void deleteCheckout();

    void changeCheckoutAddress (long id);

    void changeCheckoutPaymentMethod (long id);

    List<CheckoutUserAddressDTO> getAllAddresses();

    void createAddress(CreateAddressDTO createAddressDTO);

    List<PaymentMethodDTO> getAllPaymentMethods();

    void createPaymentMethod(CreatePaymentMethodDTO createPaymentMethodDTO);

    void generateOrder();

}
