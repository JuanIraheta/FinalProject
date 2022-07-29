package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.PaymentMethod;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.web.DTO.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreator {

    public User createUser ()
    {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .userName("tester")
                .firstName("Juan")
                .lastName("Iraheta")
                .phoneNumber("+503 71299 9991")
                .address(createAddressList())
                .paymentMethods(createPaymentMethodList())
                .build();
    }

    public UserDTO createUserDTO ()
    {
        return UserDTO.builder()
                .email(("test@test.com"))
                .userName("tester")
                .firstName("Juan")
                .lastName("Iraheta")
                .phoneNumber("+503 71299 9991")
                .address(createCreateAddressDTOList())
                .paymentMethods(createPaymentMethodNoIdDTOList())
                .build();


    }

    public List<Address> createAddressList()
    {
        List<Address> addresses = new ArrayList<>();
        addresses.add(createAddress());
        return addresses;
    }
    public Address createAddress()
    {
        return Address.builder()
                .id(1L)
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    public List<CreateAddressDTO> createCreateAddressDTOList()
    {
        List<CreateAddressDTO> createAddressDTOList = new ArrayList<>();
        createAddressDTOList.add(createCreateAddressDTO());
        return createAddressDTOList;
    }

    public CreateAddressDTO createCreateAddressDTO()
    {
        return CreateAddressDTO.builder()
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    public List<CheckoutUserAddressDTO> createCheckoutUserAddressDTOList()
    {
        List<CheckoutUserAddressDTO> checkoutUserAddressDTOList = new ArrayList<>();
        checkoutUserAddressDTOList.add(createCheckoutUserAddressDTO());
        return checkoutUserAddressDTOList;
    }

    public CheckoutUserAddressDTO createCheckoutUserAddressDTO()
    {
        return CheckoutUserAddressDTO.builder()
                .id(1L)
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    public List<PaymentMethod> createPaymentMethodList()
    {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(createPaymentMethod());
        return paymentMethods;
    }
    public PaymentMethod createPaymentMethod()
    {
        return PaymentMethod.builder()
                .id(1L)
                .name("name")
                .founds(100.00)
                .paymentType("type")
                .build();
    }

    public CreatePaymentMethodDTO createCreatePaymentMethodDTO()
    {
        return CreatePaymentMethodDTO.builder()
                .name("name")
                .founds(100.00)
                .paymentType("type")
                .build();
    }

    public List<PaymentMethodNoIdDTO> createPaymentMethodNoIdDTOList()
    {
        List<PaymentMethodNoIdDTO> paymentMethodNoIdDTOList = new ArrayList<>();
        paymentMethodNoIdDTOList.add(createPaymentMethodNoIdDTO());
        return paymentMethodNoIdDTOList;
    }

    public PaymentMethodNoIdDTO createPaymentMethodNoIdDTO ()
    {
        return PaymentMethodNoIdDTO.builder()
                .name("name")
                .paymentType("type")
                .build();
    }

    public List<PaymentMethodDTO> createPaymentMethodDTOList()
    {
        List<PaymentMethodDTO> paymentMethodDTOS = new ArrayList<>();
        paymentMethodDTOS.add(createPaymentMethodDTO());
        return paymentMethodDTOS;
    }

    public PaymentMethodDTO createPaymentMethodDTO ()
    {
        return PaymentMethodDTO.builder()
                .id(1L)
                .name("name")
                .paymentType("type")
                .build();
    }
}
