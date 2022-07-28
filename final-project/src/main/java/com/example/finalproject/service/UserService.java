package com.example.finalproject.service;

import com.example.finalproject.web.DTO.*;

import java.util.List;

public interface UserService {
//    List<UserDTO>getAllUsers();

    UserDTO getUser(String email);

    List<CheckoutUserAddressDTO> getAllAddresses(String email);

    void createAddress(String email, CreateAddressDTO createAddressDTO);

    List<PaymentMethodDTO> getAllPaymentMethods(String email);

    void createPaymentMethod(String email, CreatePaymentMethodDTO createPaymentMethodDTO);
}
