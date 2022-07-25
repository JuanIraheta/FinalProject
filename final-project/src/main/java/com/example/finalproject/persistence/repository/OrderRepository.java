package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {
}
