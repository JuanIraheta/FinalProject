package com.example.finalproject.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDTO {
    //DTO used for showing important information about the user

    private String email;

    private String userName;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private List<CreateAddressDTO> address;

    private List<PaymentMethodNoIdDTO> paymentMethods;

}
