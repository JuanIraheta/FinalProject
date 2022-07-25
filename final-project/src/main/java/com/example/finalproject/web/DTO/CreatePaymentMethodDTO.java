package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePaymentMethodDTO {
//Dto used to create a payment method on the checkout

    private String name;

    private double founds;

    private String paymentType;

}
