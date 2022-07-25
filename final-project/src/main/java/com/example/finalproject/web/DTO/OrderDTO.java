package com.example.finalproject.web.DTO;

import com.example.finalproject.persistence.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDTO {
//Dto used to show the information of an specific order

    private long id;

//    private User user;
    private String firstName;

    private String lastName;

    private CreateAddressDTO address;

    private PaymentMethodNoIdDTO paymentMethod;

    private List<OrderProductDTO> orderProducts;

    private double total;

    private boolean delivered;
}
