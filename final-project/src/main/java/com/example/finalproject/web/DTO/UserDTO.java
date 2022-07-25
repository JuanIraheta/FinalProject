package com.example.finalproject.web.DTO;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
