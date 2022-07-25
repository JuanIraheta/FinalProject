package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class CreateAddressDTO {
//Dto used for creating an address in the checkout

    @Column
    @NotBlank(message = "House Number is Mandatory")
    private String houseNumber;

    @Column
    @NotBlank(message = "Street is Mandatory")
    private String street;

    @Column
    @NotBlank(message = "City is Mandatory")
    private String city;

    @Column
    @NotBlank(message = "State is Mandatory")
    private String state;

}

