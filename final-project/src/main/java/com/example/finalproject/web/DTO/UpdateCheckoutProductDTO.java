package com.example.finalproject.web.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateCheckoutProductDTO {
//DTO to update the checkout product quantity
    private int quantity;


    @JsonCreator
    public UpdateCheckoutProductDTO(int quantity) {
        this.quantity = quantity;
    }
}
