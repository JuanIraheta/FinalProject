package com.example.finalproject.persistence.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "CheckoutProduct")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    @NotNull(message = "Stock is Mandatory")
    @PositiveOrZero
    private int quantity;

}
