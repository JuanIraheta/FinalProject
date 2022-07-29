package com.example.finalproject.web.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateCheckoutDTO {
//Dto used to create a checkout with basic information.

    @NotEmpty(message = "Add at least one product")
    @Valid
    private List<CreateCheckoutProductDTO> products;

    @JsonCreator
    public CreateCheckoutDTO(List<CreateCheckoutProductDTO> products) {
        this.products = products;
    }

}
