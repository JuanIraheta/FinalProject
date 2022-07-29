package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
public class CreateCheckoutProductDTO {
//DTO used for creating a checkout product

    @NotNull(message = "Product id is mandatory")
    private long id;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be a positive value")
    private int quantity;
}
