package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckoutProductDTO {

    private String productName;

    private int quantity;
}
