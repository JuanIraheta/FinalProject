package com.example.finalproject.web.DTO;

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

}
