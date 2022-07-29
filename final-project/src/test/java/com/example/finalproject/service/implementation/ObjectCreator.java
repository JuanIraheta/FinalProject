package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.*;
import com.example.finalproject.web.DTO.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreator {

    //USER
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

    //ADDRESS
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

    private CreateAddressDTO createAddressDTO()
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

    //PAYMENT METHOD
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

    //PRODUCT OBJECTS
    public Product createProduct()
    {
        return Product.builder()
                .id(1L)
                .name("product")
                .stock(1)
                .price(1.00)
                .build();
    }

    public ProductDTO createProductDTO ()
    {
        return ProductDTO.builder()
                .id(1L)
                .name("product")
                .stock(1)
                .price(1.00)
                .build();
    }

    //ORDER

    public Orders createOrder ()
    {
        return Orders.builder()
                .id(1L)
                .user(createUser())
                .address(createAddress())
                .paymentMethod(createPaymentMethod())
                .orderProducts(createOrderProducts())
                .total(1.00)
                .build();
    }

    public OrderDTO createOrderDTO ()
    {
        return OrderDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Iraheta")
                .address(createAddressDTO())
                .orderProducts(createListOrderProductDTO())
                .paymentMethod(createPaymentMethodNoIdDTO())
                .total(1.00)
                .delivered(false)
                .build();
    }

    //ORDER PRODUCT
    public List<OrderProduct> createOrderProducts()
    {
        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .product(createProduct())
                .quantity(1)
                .build();
        orderProducts.add(orderProduct);
        return orderProducts;
    }

    public List<OrderProductDTO> createListOrderProductDTO ()
    {
        List<OrderProductDTO> list = new ArrayList<>();
        OrderProductDTO orderProductDTO = createOrderProductDTO();
        list.add(orderProductDTO);
        return list;
    }

    public OrderProductDTO createOrderProductDTO()
    {
        return OrderProductDTO.builder()
                .name("product")
                .price(1.00)
                .quantity(1)
                .build();
    }

    //Checkout
    public Checkout createCheckout ()
    {
        return Checkout.builder()
                .id(1L)
                .user(createUser())
                .checkoutProducts(createCheckoutProductList())
                .address(createAddress())
                .paymentMethod(createPaymentMethod())
                .build();
    }

    public CheckoutDTO createCheckoutDTO()
    {
        return CheckoutDTO.builder()
                .userName("tester")
                .firstName("Juan")
                .lastName("Iraheta")
                .checkoutProducts(createProductCheckoutDTOList())
                .address(createCreateAddressDTO())
                .paymentMethod(createPaymentMethodNoIdDTO())
                .subTotal(1.00)
                .build();
    }

    //CHECKOUT PRODUCTS
    public List<CheckoutProduct> createCheckoutProductList ()
    {
        List<CheckoutProduct> checkoutProductList = new ArrayList<>();
        checkoutProductList.add(createCheckoutProduct());
        return checkoutProductList;
    }
    public CheckoutProduct createCheckoutProduct ()
    {
        return CheckoutProduct.builder()
                .id(1L)
                .product(createProduct())
                .quantity(1)
                .build();
    }

    public List<ProductCheckoutDTO> createProductCheckoutDTOList()
    {
        List<ProductCheckoutDTO> productCheckoutDTOList = new ArrayList<>();
        productCheckoutDTOList.add(createProductCheckoutDTO());
        return productCheckoutDTOList;
    }
    public ProductCheckoutDTO createProductCheckoutDTO()
    {
        return ProductCheckoutDTO.builder()
                .name("product")
                .price(1.00)
                .quantity(1)
                .build();
    }
}
