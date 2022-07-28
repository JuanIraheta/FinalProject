package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class CheckoutUserAddressDTO {
//Dto used for geting an address from the checkout

    private long id;


    @NotBlank(message = "House Number is Mandatory")
    private String houseNumber;


    @NotBlank(message = "Street is Mandatory")
    private String street;


    @NotBlank(message = "City is Mandatory")
    private String city;


    @NotBlank(message = "State is Mandatory")
    private String state;
}
