package com.example.finalproject.service.mapper;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.web.DTO.CheckoutDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CheckoutMapper {

    CheckoutMapper INSTANCE = Mappers.getMapper(CheckoutMapper.class);

    @Mappings({
            @Mapping(target = "userName", source = "checkout.user.userName")
    })
    CheckoutDTO checkoutToCheckoutDTO(Checkout checkout);
}
