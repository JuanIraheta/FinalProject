package com.example.finalproject.web.DTO;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.CheckoutProduct;
import com.example.finalproject.persistence.model.PaymentMethod;
import com.example.finalproject.persistence.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
public class CheckoutDTO {
//DTO to show on JSON all the info of a checkout

    private String userName;

    private String firstName;

    private String lastName;

    private List<ProductCheckoutDTO> checkoutProducts;

    private CreateAddressDTO address;

    private PaymentMethodNoIdDTO paymentMethod;

    private double subTotal;
}
