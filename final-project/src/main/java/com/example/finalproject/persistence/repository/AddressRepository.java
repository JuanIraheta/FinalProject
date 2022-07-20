package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
