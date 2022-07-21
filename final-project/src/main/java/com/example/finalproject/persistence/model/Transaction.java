package com.example.finalproject.persistence.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    private double quantity;

    @ManyToOne
    private PaymentMethod paymentMethod;
}
