package com.example.finalproject.web.DTO;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.persistence.model.CheckoutProduct;
import com.example.finalproject.persistence.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateCheckoutDTO {

    private long userID;

    private List<CheckoutProductDTO> products;
//    private String product;
//
//    private int quantity;

}
