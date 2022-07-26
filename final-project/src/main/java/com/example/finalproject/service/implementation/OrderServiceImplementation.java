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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderDTO> getAllOrders()
    {
        User user = getUser(1L);
        List<Orders> orders = orderRepository.findAllByUser(user);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Orders order: orders)
        {
            OrderDTO dto = OrderMapper.INSTANCE.orderToOrderDTO(order);

            mappingOrderProducts(dto,order);

            orderDTOS.add(dto);
        }

        return orderDTOS;
    }

    @Override
    public OrderDTO getOrder(long id)
    {
        User user = getUser(1L);
        Orders order = orderRepository.findByUserAndId(user,id);
        OrderDTO orderDTO = OrderMapper.INSTANCE.orderToOrderDTO(order);

        mappingOrderProducts(orderDTO,order);

        return orderDTO;
    }

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



    private User getUser (long id)
    {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user.get();
    }
}
