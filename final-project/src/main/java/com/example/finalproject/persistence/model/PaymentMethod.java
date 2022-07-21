package com.example.finalproject.persistence.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PaymentMethod")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    private double founds;

    private String paymentType;
}
