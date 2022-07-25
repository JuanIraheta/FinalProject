package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckoutProductDTO {
//DTO used for creating a checkout product

    private String productName;

    private int quantity;
}
