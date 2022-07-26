package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCheckoutDTO {
//DTO used to show  only some information of the product checkout
    private String name;

    private double price;

    private int quantity;


}
