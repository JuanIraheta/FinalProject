package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.PaymentMethod;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.AddressRepository;
import com.example.finalproject.persistence.repository.PaymentMethodRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.UserService;
import com.example.finalproject.service.mapper.AddressMapper;
import com.example.finalproject.service.mapper.PaymentMethodMapper;
import com.example.finalproject.service.mapper.UserMapper;
import com.example.finalproject.web.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;

//    @Override
//    public List<UserDTO> getAllUsers() {
//
//        List<User> users = userRepository.findAll();
//        return UserMapper.INSTANCE.usersToUserDTOS(users);
//    }

    @Override
    public UserDTO getUser(String userEmail) {
        User foundUser = foundUser(userEmail);
        return UserMapper.INSTANCE.userToUserDTO(foundUser);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods(String email)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = foundUser(email);
        //Find all the payment method related to the user
        List<PaymentMethod> getPaymentMethods = paymentMethodRepository.findAllByUser(user);
        if (getPaymentMethods.isEmpty())
        {
            throw new ResourceNotFoundException("There are no payment methods in this user, try to create one");
        }

        return PaymentMethodMapper.INSTANCE.paymentMethodToPaymentMethodDTO(getPaymentMethods);
    }

    @Override
    public void createPaymentMethod(String email, CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = foundUser(email);
        //Mapping the dto to a payment method
        PaymentMethod createPaymentMethod = PaymentMethodMapper.INSTANCE.createPaymentMethodDTOToPaymentMethod(createPaymentMethodDTO);
        createPaymentMethod.setUser(user);
        //saving the payment method
        paymentMethodRepository.save(createPaymentMethod);
    }

    @Override
    public List<UserAddressDTO> getAllAddresses(String email)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = foundUser(email);
        //Find all the addresses related to the user
        List<Address> getAddresses = addressRepository.findAllByUser(user);
        if (getAddresses.isEmpty())
        {
            throw new ResourceNotFoundException("There are no addresses in this user, try to create one");
        }

        return AddressMapper.INSTANCE.addressToCheckoutUserAddressDTO(getAddresses);
    }

    @Override
    public void createAddress(String email, CreateAddressDTO createAddressDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = foundUser(email);
        //Mapping the dto to an address
        Address createAddress = AddressMapper.INSTANCE.createAddressDTOToAddress(createAddressDTO);
        createAddress.setUser(user);
        //saving the address
        addressRepository.save(createAddress);
    }

    private User foundUser (String email)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user;
    }


}
