package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateCheckoutDTO {

    private long userID;

    @NotEmpty(message = "Add at least one product")
    private List<CheckoutProductDTO> products;

}
