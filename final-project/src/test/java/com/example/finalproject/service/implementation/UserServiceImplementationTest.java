package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.Address;
import com.example.finalproject.persistence.model.PaymentMethod;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.AddressRepository;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.PaymentMethodRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.web.DTO.*;
import org.aspectj.weaver.ast.Or;
import org.h2.command.ddl.CreateUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    private UserServiceImplementation userServiceImplementation;

    @BeforeEach
    private void setUp ()
    {
        userServiceImplementation = new UserServiceImplementation(userRepository,addressRepository,paymentMethodRepository);
    }

    @Nested
    @DisplayName("getUser")
    class getUser {

        @Test
        @DisplayName("getUser When valid email return valid UserDTO")
        void getUser_ValidUserEmail_GetUserDTO()
        {
            User user = createUser();
            UserDTO userDTO = createUserDTO();
            
            when(userRepository.findByEmail(anyString())).thenReturn(user);
            
            UserDTO getUserDTO = userServiceImplementation.getUser("irahetajuanjose@hotmail.com");

            assertThat(getUserDTO.getEmail(),is(userDTO.getEmail()));
            assertThat(getUserDTO.getUserName(),is(userDTO.getUserName()));
            assertThat(getUserDTO.getAddress().size(),is(userDTO.getAddress().size()));
            assertThat(getUserDTO.getPaymentMethods().size(),is(userDTO.getPaymentMethods().size()));

            verify(userRepository).findByEmail(anyString());
        }

        @Test
        @DisplayName("getUser When invalid email throw exception")
        void getUser_InValidUserEmail_ThrowException()
        {
            when(userRepository.findByEmail(anyString())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    userServiceImplementation.getUser("email@test.com"));

            verify(userRepository).findByEmail(anyString());
        }
    }

    @Nested
    @DisplayName("getAllPaymentMethods")
    class getAllPaymentMethods {
        @Test
        @DisplayName("getAllPaymentMethods When valid email return List Payment Method")
        void getAllPaymentMethods_ValidUserEmail_ListPaymentMethods()
        {
            User user = createUser();
            List<PaymentMethod> paymentMethodList = createPaymentMethodList();
            List<PaymentMethodDTO> paymentMethodDTOList = createPaymentMethodDTOList();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(paymentMethodRepository.findAllByUser(eq(user))).thenReturn(paymentMethodList);

            List<PaymentMethodDTO> getAllPaymentMethodsDTO= userServiceImplementation.getAllPaymentMethods("test@test.com");

            assertThat(getAllPaymentMethodsDTO.size(),is(paymentMethodDTOList.size()));

            verify(userRepository).findByEmail(anyString());
            verify(paymentMethodRepository).findAllByUser(eq(user));
        }

        @Test
        @DisplayName("getAllPaymentMethods When no payment methods Throw Exception")
        void getAllPaymentMethods_NoPaymentMethodAvailable_ThrowException()
        {
            User user = createUser();
            List<PaymentMethod> paymentMethodList = new ArrayList<>();
            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(paymentMethodRepository.findAllByUser(eq(user))).thenReturn(paymentMethodList);

            assertThrows(ResourceNotFoundException.class,() ->
                    userServiceImplementation.getAllPaymentMethods("email@test.com"));

            verify(userRepository).findByEmail(anyString());
            verify(paymentMethodRepository).findAllByUser(eq(user));
        }
    }

    @Test
    @DisplayName("createPaymentMethod When valid dto create a payment method")
    void createPaymentMethod_ValidCreatePaymentMethodDTO_CreatePaymentMethod()
    {
        User user = createUser();
        CreatePaymentMethodDTO createPaymentMethodDTO = createCreatePaymentMethodDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        userServiceImplementation.createPaymentMethod("test@test.com",createPaymentMethodDTO);

        verify(userRepository).findByEmail(anyString());
    }

    @Nested
    @DisplayName("getAllAddresses")
    class getAllAddresses {
        @Test
        @DisplayName("getAllAddresses When valid email return List Addresses")
        void getAllAddresses_ValidUserEmail_ListAddresses()
        {
            User user = createUser();
            List<Address> addressList = createAddressList();

            List<CheckoutUserAddressDTO> checkoutUserAddressDTOList = createCheckoutUserAddressDTOList();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(addressRepository.findAllByUser(eq(user))).thenReturn(addressList);

            List<CheckoutUserAddressDTO> getCheckoutUserAddressDtoList =
                    userServiceImplementation.getAllAddresses("test@test.com");

            assertThat(getCheckoutUserAddressDtoList.size(),is(checkoutUserAddressDTOList.size()));

            verify(userRepository).findByEmail(anyString());
            verify(addressRepository).findAllByUser(eq(user));
        }

        @Test
        @DisplayName("getAllAddresses When No address available Throw Exception")
        void getAllAddresses_NoAddressAvailable_ThrowException()
        {
            User user = createUser();
            List<Address> addressList = new ArrayList<>();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(addressRepository.findAllByUser(eq(user))).thenReturn(addressList);

            assertThrows(ResourceNotFoundException.class,() ->
                    userServiceImplementation.getAllAddresses("email@test.com"));

            verify(userRepository).findByEmail(anyString());
            verify(addressRepository).findAllByUser(user);
        }
    }

    @Test
    @DisplayName("createAddress When valid CreateAddressDTO create new address")
    void createAddress_ValidCreateAddressDTO_CreateAddress()
    {
        User user = createUser();
        CreateAddressDTO createAddressDTO = createCreateAddressDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        userServiceImplementation.createAddress("test@test.com",createAddressDTO);

        verify(userRepository).findByEmail(anyString());
    }


    private User createUser ()
    {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .userName("tester")
                .firstName("Juan")
                .lastName("Iraheta")
                .phoneNumber("+503 71299 9991")
                .address(createAddressList())
                .paymentMethods(createPaymentMethodList())
                .build();
    }

    private UserDTO createUserDTO ()
    {
        return UserDTO.builder()
                .email(("test@test.com"))
                .userName("tester")
                .firstName("Juan")
                .lastName("Iraheta")
                .phoneNumber("+503 71299 9991")
                .address(createCreateAddressDTOList())
                .paymentMethods(createPaymentMethodNoIdDTOList())
                .build();


    }

    private List<Address> createAddressList()
    {
        List<Address> addresses = new ArrayList<>();
        addresses.add(createAddress());
        return addresses;
    }
    private Address createAddress()
    {
        return Address.builder()
                .id(1L)
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    private List<CreateAddressDTO> createCreateAddressDTOList()
    {
        List<CreateAddressDTO> createAddressDTOList = new ArrayList<>();
        createAddressDTOList.add(createCreateAddressDTO());
        return createAddressDTOList;
    }

    private CreateAddressDTO createCreateAddressDTO()
    {
        return CreateAddressDTO.builder()
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    private List<CheckoutUserAddressDTO> createCheckoutUserAddressDTOList()
    {
        List<CheckoutUserAddressDTO> checkoutUserAddressDTOList = new ArrayList<>();
        checkoutUserAddressDTOList.add(createCheckoutUserAddressDTO());
        return checkoutUserAddressDTOList;
    }

    private CheckoutUserAddressDTO createCheckoutUserAddressDTO()
    {
        return CheckoutUserAddressDTO.builder()
                .id(1L)
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    private List<PaymentMethod> createPaymentMethodList()
    {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(createPaymentMethod());
        return paymentMethods;
    }
    private PaymentMethod createPaymentMethod()
    {
        return PaymentMethod.builder()
                .id(1L)
                .name("name")
                .founds(100.00)
                .paymentType("type")
                .build();
    }

    private CreatePaymentMethodDTO createCreatePaymentMethodDTO()
    {
        return CreatePaymentMethodDTO.builder()
                .name("name")
                .founds(100.00)
                .paymentType("type")
                .build();
    }

    private List<PaymentMethodNoIdDTO> createPaymentMethodNoIdDTOList()
    {
        List<PaymentMethodNoIdDTO> paymentMethodNoIdDTOList = new ArrayList<>();
        paymentMethodNoIdDTOList.add(createPaymentMethodNoIdDTO());
        return paymentMethodNoIdDTOList;
    }

    private PaymentMethodNoIdDTO createPaymentMethodNoIdDTO ()
    {
        return PaymentMethodNoIdDTO.builder()
                .name("name")
                .paymentType("type")
                .build();
    }

    private List<PaymentMethodDTO> createPaymentMethodDTOList()
    {
        List<PaymentMethodDTO> paymentMethodDTOS = new ArrayList<>();
        paymentMethodDTOS.add(createPaymentMethodDTO());
        return paymentMethodDTOS;
    }

    private PaymentMethodDTO createPaymentMethodDTO ()
    {
        return PaymentMethodDTO.builder()
                .id(1L)
                .name("name")
                .paymentType("type")
                .build();
    }
}