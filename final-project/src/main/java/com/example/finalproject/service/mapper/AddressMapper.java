package com.example.finalproject.service.mapper;

import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.web.DTO.CheckoutUserAddressDTO;
import com.example.finalproject.web.DTO.CreateAddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    CheckoutUserAddressDTO AddressToCheckoutUserAddressDTO (Address address);

    Address CreateAddressDTOToAddress(CreateAddressDTO createAddressDTO);
}
