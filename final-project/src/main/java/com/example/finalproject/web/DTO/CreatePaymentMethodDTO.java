package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
public class CreatePaymentMethodDTO {
//Dto used to create a payment method on the checkout


    @NotBlank(message = "Name is Mandatory")
    private String name;

    @PositiveOrZero(message = "Founds must be positive or zero")
    private double founds;

    @NotBlank(message = "Payment type is mandatory")
    private String paymentType;

}
