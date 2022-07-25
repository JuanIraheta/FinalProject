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


    private String userName;

    private List<CheckoutProduct> checkoutProducts;

    private Address address;

    private PaymentMethod paymentMethod;

    private double subTotal;
}
