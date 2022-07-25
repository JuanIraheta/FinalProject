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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

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
