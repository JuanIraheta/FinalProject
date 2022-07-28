package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
public class CheckoutProductDTO {
//DTO used for creating a checkout product

    @NotEmpty(message = "Product name is mandatory")
    private String productName;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be a positive value")
    private int quantity;
}
