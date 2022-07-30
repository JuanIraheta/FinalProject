package com.example.finalproject.web.DTO;

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

}
