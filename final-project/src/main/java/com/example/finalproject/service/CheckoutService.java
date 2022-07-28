package com.example.finalproject.service;

import com.example.finalproject.web.DTO.*;

import java.util.List;

public interface CheckoutService {

    CheckoutDTO getCheckout(String email);
    void createCheckOut(String email,CreateCheckoutDTO checkoutDTO);

    void addProductToCheckout(String email,CheckoutProductDTO checkoutProductDTO);

    void modifyCheckoutProductQuantity(String email,String productName, UpdateCheckoutProductDTO updateCheckoutProductDTO);

    void deleteCheckoutProduct(String email,String productName);

    void deleteCheckout(String email);

    void changeCheckoutAddress (String email,long id);

    void changeCheckoutPaymentMethod (String email,long id);

    List<CheckoutUserAddressDTO> getAllAddresses(String email);

    void createAddress(String email,CreateAddressDTO createAddressDTO);

    List<PaymentMethodDTO> getAllPaymentMethods(String email);

    void createPaymentMethod(String email,CreatePaymentMethodDTO createPaymentMethodDTO);

    void generateOrder(String email);

}
