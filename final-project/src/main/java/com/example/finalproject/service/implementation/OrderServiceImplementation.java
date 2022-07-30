package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.OrderProduct;
import com.example.finalproject.persistence.model.Orders;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.OrderRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.OrderService;
import com.example.finalproject.service.mapper.OrderMapper;
import com.example.finalproject.web.DTO.OrderDTO;
import com.example.finalproject.web.DTO.OrderProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderDTO> getAllOrders(String email)
    {
        //Get the valid user from database
        User user = getUser(email);
        //Find a list of orders from the user
        List<Orders> orders = orderRepository.findAllByUser(user);
        if (orders.isEmpty())
        {
            throw new ResourceNotFoundException("There are no orders in this user");
        }
        //Mapping the all the orders to its DTO
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Orders order: orders)
        {
            //Mapping the order
            OrderDTO dto = OrderMapper.INSTANCE.orderToOrderDTO(order);

            //Mapping the order products to its dto
            mappingOrderProducts(dto,order);

            //Adding the dtos to the dto list
            orderDTOS.add(dto);
        }

        return orderDTOS;
    }

    @Override
    public OrderDTO getOrder(String email, long id)
    {
        //getting a valid user
        User user = getUser(email);
        //getting the specific order from the user and the id
        Orders order = orderRepository.findByUserAndId(user,id);
        if (order == null)
        {
            throw new ResourceNotFoundException("Order not found");
        }
        //Mapping the specific order to its dto
        OrderDTO orderDTO = OrderMapper.INSTANCE.orderToOrderDTO(order);

        //mapping the products of the order to its dto
        mappingOrderProducts(orderDTO,order);

        return orderDTO;
    }

    //Method used to map the specific order products to an order product dto
    private void mappingOrderProducts (OrderDTO orderDTO, Orders order)
    {
        List<OrderProductDTO> orderProductDTOS = new ArrayList<>();

        for (OrderProduct orderProduct: order.getOrderProducts())
        {
            OrderProductDTO dto = OrderMapper.INSTANCE.orderProductAndProductToOrderProductDTO
                    (orderProduct,orderProduct.getProduct());
            orderProductDTOS.add(dto);
        }
        orderDTO.setOrderProducts(orderProductDTOS);
    }


    //get an specific user from the database
    private User getUser (String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user;
    }
}
