package com.example.finalproject.persistence.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Table(name = "Order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    private User user;


    @OneToOne
    private Address address;

    @OneToMany
    private List<OrderProduct> orderProducts;

    @PositiveOrZero(message = "Total must be a positive Value")
    private double total;

    @OneToOne
    private Transaction transaction;


}
