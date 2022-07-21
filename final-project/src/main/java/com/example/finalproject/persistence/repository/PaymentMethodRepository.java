package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {
}
