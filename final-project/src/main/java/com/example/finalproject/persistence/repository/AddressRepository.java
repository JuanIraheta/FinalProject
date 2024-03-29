package com.example.finalproject.persistence.repository;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {

    Address findByUserAndId(User user, Long id);
}
