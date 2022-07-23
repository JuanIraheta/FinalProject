package com.example.finalproject.service.implementation;

import com.example.finalproject.persistence.model.Checkout;
import com.example.finalproject.persistence.model.CheckoutProduct;
import com.example.finalproject.persistence.model.Product;
import com.example.finalproject.persistence.model.User;
import com.example.finalproject.persistence.repository.CheckoutProductRepository;
import com.example.finalproject.persistence.repository.CheckoutRepository;
import com.example.finalproject.persistence.repository.ProductRepository;
import com.example.finalproject.persistence.repository.UserRepository;
import com.example.finalproject.service.CheckoutService;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImplementation implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final CheckoutProductRepository checkoutProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }

    public void createCheckOut(CreateCheckoutDTO checkoutDTO)
    {
        //Crear el checkout basico
        Optional<User> getUser = userRepository.findById(checkoutDTO.getUserID());
        if (!getUser.isPresent())
        {
            //Throw Exception
        }

        //Creating checkout
        Checkout checkout = Checkout.builder()
                .user(getUser.get())
                .address(getUser.get().getAddress().size() > 0 ? getUser.get().getAddress().get(0):null)
                .paymentMethod(getUser.get().getPaymentMethods().size() > 0 ?
                        getUser.get().getPaymentMethods().get(0) : null)
                .build();

        Checkout savedCheckout = checkoutRepository.save(checkout);


        //Crear los respectivos checkout products basandose en la lista de products.
        for (int i= 0; i < checkoutDTO.getProducts().size();i++)
        {

            Product getProduct = productRepository.findByName(checkoutDTO.getProducts().get(i).getProductName());
            if (getProduct == null)
            {
                //Throw Exception
            }
            CheckoutProduct checkoutProduct = CheckoutProduct.builder()
                    .product(getProduct)
                    .quantity(checkoutDTO.getProducts().get(i).getQuantity() < getProduct.getStock()
                            ? checkoutDTO.getProducts().get(i).getQuantity() : 0)
//                    .quantity(checkoutDTO.getProducts().get(i).getQuantity())
                    .checkout(savedCheckout)
                    .build();
//            checkoutProduct.setProduct(getProduct);
//            checkoutProduct.setQuantity(checkoutDTO.getProducts().get(i).getQuantity());
//            checkoutProduct.setCheckout(savedCheckout);

            checkoutProductRepository.save(checkoutProduct);
        }


    }

    public void createCheckoutProduct()
    {
        CheckoutProduct checkoutProduct = new CheckoutProduct();
        Product getProduct = productRepository.findById(1l).get();
        checkoutProduct.setProduct(getProduct);
        checkoutProduct.setQuantity(1);

        checkoutProductRepository.save(checkoutProduct);
    }
}
