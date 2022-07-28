package com.example.finalproject.web.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UpdateCheckoutProductDTO {
//DTO to update the checkout product quantity

    @NotNull(message = "Quantity is mandatory")
    private int quantity;


    @JsonCreator
    public UpdateCheckoutProductDTO(int quantity) {
        this.quantity = quantity;
    }
}
