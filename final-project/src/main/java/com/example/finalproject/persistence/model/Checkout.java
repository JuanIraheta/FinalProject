package com.example.finalproject.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Checkout")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @OneToOne
    private User user;

    @Column
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "checkout_id",referencedColumnName = "id")
    private List<CheckoutProduct>checkoutProducts;

    @OneToOne
    private Address address;

}
