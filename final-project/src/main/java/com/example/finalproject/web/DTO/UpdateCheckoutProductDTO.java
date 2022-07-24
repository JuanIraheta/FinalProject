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

//    private String productName;

    private int quantity;


    @JsonCreator
    public UpdateCheckoutProductDTO(int quantity) {
        this.quantity = quantity;
    }
}
