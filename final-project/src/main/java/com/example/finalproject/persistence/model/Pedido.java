package com.example.finalproject.persistence.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;
//
//    @ManyToOne
//    private User user;
//
//    @OneToOne
//    private Address address;
//
//    @OneToMany
//    private List<OrderProduct> orderProducts;
//
//    @PositiveOrZero(message = "Total must be a positive Value")
//    private double total;
//
//    @OneToOne
//    private Transaction transaction;


}
