package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.OrderProduct;
import com.example.finalproject.persistence.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository  extends JpaRepository<OrderProduct,Long> {
}
