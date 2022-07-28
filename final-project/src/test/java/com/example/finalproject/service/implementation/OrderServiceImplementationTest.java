package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.*;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.web.DTO.CreateAddressDTO;
import com.example.finalproject.web.DTO.OrderDTO;
import com.example.finalproject.web.DTO.OrderProductDTO;
import com.example.finalproject.web.DTO.PaymentMethodNoIdDTO;
import org.aspectj.weaver.ast.Or;
import org.h2.command.ddl.CreateUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
class OrderServiceImplementationTest {
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  UserRepository userRepository;

    private OrderServiceImplementation orderServiceImplementation;

    @BeforeEach
    private void setUp ()
    {
        orderServiceImplementation = new OrderServiceImplementation(orderRepository,userRepository);
    }


    @Nested
    @DisplayName("getOrder")
    class getOrder {
        @Test
        @DisplayName("getOrder When valid email return valid OrderDTO")
        void getOrder_ValidUserEmail_GetOrderDTO()
        {
            User user = createUser();
            Orders order = createOrder();
            OrderDTO orderDTO = createOrderDTO();

            when(orderRepository.findByUserAndId(any(),anyLong())).thenReturn(order);
            when(userRepository.findByEmail(any())).thenReturn(user);

            OrderDTO getOrderDTO = orderServiceImplementation.getOrder("irahetajuanjose@hotmail.com",1L);

            assertThat(getOrderDTO.getId(),is(orderDTO.getId()));
            assertThat(getOrderDTO.getFirstName(),is(orderDTO.getFirstName()));
            assertThat(getOrderDTO.getLastName(),is(orderDTO.getLastName()));
            assertThat(getOrderDTO.getAddress().getCity(),is(orderDTO.getAddress().getCity()));
            assertThat(getOrderDTO.getPaymentMethod().getPaymentType(),is(orderDTO.getPaymentMethod().getPaymentType()));
            assertThat(getOrderDTO.getOrderProducts().size(),is(orderDTO.getOrderProducts().size()));
            assertThat(getOrderDTO.getTotal(),is(orderDTO.getTotal()));

            verify(orderRepository).findByUserAndId(any(),anyLong());
            verify(userRepository).findByEmail(any());
        }

        @Test
        @DisplayName("getOrder When invalid email throw Exception")
        void getOrder_InValidUserEmail_ThrowException()
        {
            when(userRepository.findByEmail(any())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    orderServiceImplementation.getOrder("email@test.com",1L));

            verify(userRepository).findByEmail(any());
        }

        @Test
        @DisplayName("getOrder When invalid order id throw Exception")
        void getOrder_InValidOrderId_ThrowException()
        {
            User user = createUser();
            when(userRepository.findByEmail(any())).thenReturn(user);
            when(orderRepository.findByUserAndId(any(),anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    orderServiceImplementation.getOrder("email@test.com",1L));

            verify(userRepository).findByEmail(any());
            verify(orderRepository).findByUserAndId(any(),anyLong());
        }
    }

    @Nested
    @DisplayName("getAllOrders")
    class getAllOrders {
        @Test
        @DisplayName("getAllOrders When valid email return valid OrderDTO")
        void getAllOrders_ValidUserEmail_GetListOrderDTO()
        {
            User user = createUser();
            Orders order = createOrder();
            List<Orders> ordersList = new ArrayList<>();
            ordersList.add(order);
            OrderDTO orderDTO = createOrderDTO();
            List<OrderDTO> orderDTOList = new ArrayList<>();
            orderDTOList.add(orderDTO);

            when(userRepository.findByEmail(any())).thenReturn(user);
            when(orderRepository.findAllByUser(user)).thenReturn(ordersList);

            List<OrderDTO> getOrderDTOList = orderServiceImplementation.getAllOrders("irahetajuanjose@hotmail.com");

            assertThat(getOrderDTOList.size(),is(orderDTOList.size()));
            assertThat(getOrderDTOList.get(0).getId(),is(orderDTOList.get(0).getId()));
            assertThat(getOrderDTOList.get(0).getOrderProducts().get(0).getName()
                    ,is(orderDTOList.get(0).getOrderProducts().get(0).getName()));

            verify(orderRepository).findAllByUser(user);
            verify(userRepository).findByEmail(any());
        }

        @Test
        @DisplayName("getAllOrders When valid email return valid OrderDTO")
        void getAllOrders_InValidUserEmail_GetListOrderDTO()
        {
            when(userRepository.findByEmail(any())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    orderServiceImplementation.getAllOrders("email@test.com"));

            verify(userRepository).findByEmail(any());
        }

        @Test
        @DisplayName("getAllOrders When there are no orders throw Exception")
        void getAllOrders_NoOrders_ThrowException()
        {
            User user = createUser();
            List<Orders> orders = new ArrayList<>();
            when(userRepository.findByEmail(any())).thenReturn(user);
            when(orderRepository.findAllByUser(eq(user))).thenReturn(orders);

            assertThrows(ResourceNotFoundException.class,() ->
                    orderServiceImplementation.getAllOrders("email@test.com"));

            verify(userRepository).findByEmail(any());
            verify(orderRepository).findAllByUser(eq(user));
        }
    }


    private User createUser ()
    {
        return User.builder()
                .id(1L)
                .email("irahetajuanjose@hotmail.com")
                .userName("loading")
                .firstName("Juan")
                .lastName("Iraheta")
                .phoneNumber("+503 7129 9991")
                .build();

    }

    private Orders createOrder (){
        return Orders.builder()
                .id(1L)
                .user(createUser())
                .address(createAddress())
                .paymentMethod(createPaymentMethod())
                .orderProducts(createOrderProducts())
                .total(1.00)
                .build();
    }

    private List<OrderProduct> createOrderProducts()
    {
        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .product(createProduct())
                .quantity(1)
                .build();
        orderProducts.add(orderProduct);
        return orderProducts;
    }

    private Product createProduct ()
    {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(1.00)
                .stock(1)
                .build();
    }

    private PaymentMethod createPaymentMethod ()
    {
        return PaymentMethod.builder()
                .id(1L)
                .name("payment")
                .user(createUser())
                .paymentType("type")
                .founds(100.00)
                .build();
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

    private CreateAddressDTO createAddressDTO()
    {
        return CreateAddressDTO.builder()
                .houseNumber("house")
                .street("street")
                .city("city")
                .state("state")
                .build();
    }

    private OrderDTO createOrderDTO ()
    {
        return OrderDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Iraheta")
                .address(createAddressDTO())
                .orderProducts(createListOrderProductDTO())
                .paymentMethod(createPaymentMethodNoIdDTO())
                .total(1.00)
                .delivered(false)
                .build();
    }

    private List<OrderProductDTO> createListOrderProductDTO ()
    {
        List<OrderProductDTO> list = new ArrayList<>();
        OrderProductDTO orderProductDTO = createOrderProductDTO();
        list.add(orderProductDTO);
        return list;
    }
    private OrderProductDTO createOrderProductDTO()
    {
        return OrderProductDTO.builder()
                .name("product")
                .price(1.00)
                .quantity(1)
                .build();
    }

    private PaymentMethodNoIdDTO createPaymentMethodNoIdDTO()
    {
        return PaymentMethodNoIdDTO.builder()
                .paymentType("type")
                .name("payment")
                .build();
    }

}