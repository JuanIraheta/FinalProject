package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Pedido,Long> {
}
