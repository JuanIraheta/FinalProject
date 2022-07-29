package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.*;
import com.example.finalproject.web.DTO.CheckoutDTO;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplementationTest {

    @Mock
    private CheckoutRepository checkoutRepository;
    @Mock
    private CheckoutProductRepository checkoutProductRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private CheckoutServiceImplementation checkoutServiceImplementation;

    private ObjectCreator objectCreator;

    @BeforeEach
    private void setUp ()
    {
        checkoutServiceImplementation = new CheckoutServiceImplementation(checkoutRepository,
                checkoutProductRepository,userRepository,productRepository,addressRepository,
                paymentMethodRepository,orderRepository,orderProductRepository,transactionRepository);
        objectCreator = new ObjectCreator();
    }

    @Nested
    @DisplayName("getCheckout")
    class getCheckout {
        @Test
        @DisplayName("getCheckout When valid email return valid CheckoutDTO")
        void getCheckout_ValidUserEmail_GetCheckoutDTO()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            CheckoutDTO checkoutDTO = objectCreator.createCheckoutDTO();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);

            CheckoutDTO getCheckoutDTO = checkoutServiceImplementation.getCheckout("test@test.com");

            assertThat(getCheckoutDTO.getFirstName(),is(checkoutDTO.getFirstName()));
            assertThat(getCheckoutDTO.getPaymentMethod(),samePropertyValuesAs(checkoutDTO.getPaymentMethod()));
            assertThat(getCheckoutDTO.getAddress(),samePropertyValuesAs(checkoutDTO.getAddress()));
            assertThat(getCheckoutDTO.getCheckoutProducts().size(),is(checkoutDTO.getCheckoutProducts().size()));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
        }

        @Test
        @DisplayName("getCheckout When invalid email throw exception")
        void getCheckout_InValidUserEmail_ThrowException()
        {
            when(userRepository.findByEmail(anyString())).thenReturn(null);
            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.getCheckout("email@test.com"));

            verify(userRepository).findByEmail(anyString());
        }

        @Test
        @DisplayName("getCheckout When there is no Checkout throw exception")
        void getCheckout_NoCheckout_ThrowException()
        {
            User user = objectCreator.createUser();
            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.getCheckout("email@test.com"));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
        }

    }

    @Nested
    @DisplayName("createCheckOut")
    class createCheckOut {
        @Test
        @DisplayName("createCheckOut When valid createCheckoutDTO create a checkout")
        void createCheckOut_ValidCreateCheckoutDTO_CreateCheckout()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
//            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();
        }
    }
}