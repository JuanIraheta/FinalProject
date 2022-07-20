package com.example.finalproject.persistence.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @NotBlank(message = "Email is Mandatory")
    @Email(message = "Email must be valid")
    private String email;

    @Column
    @NotBlank(message = "First Name is Mandatory")
    private String userName;

    @Column
    @Pattern(regexp = "^[+]\\D*(503)\\D*(\\d{4})\\D*(\\d{4})" , message = "Please enter a valid phone number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Set<Address> address;
}
