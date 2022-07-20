package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckoutRepository extends JpaRepository<Checkout,Long> {
}
