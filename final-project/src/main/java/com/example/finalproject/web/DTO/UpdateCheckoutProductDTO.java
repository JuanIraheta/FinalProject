package com.example.finalproject.web.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCheckoutProductDTO {
//DTO to update the checkout product quantity

    @NotNull(message = "Quantity is mandatory")
    private int quantity;


//    @JsonCreator
//    public UpdateCheckoutProductDTO(int quantity) {
//        this.quantity = quantity;
//    }
}
