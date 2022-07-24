package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.NotEnoughStockException;
import com.example.finalproject.exception.ResourceAlreadyExistException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.*;
import com.example.finalproject.persistence.repository.*;
import com.example.finalproject.service.CheckoutService;
import com.example.finalproject.service.mapper.CheckoutMapper;
import com.example.finalproject.web.DTO.CheckoutDTO;
import com.example.finalproject.web.DTO.CheckoutProductDTO;
import com.example.finalproject.web.DTO.CreateCheckoutDTO;
import com.example.finalproject.web.DTO.UpdateCheckoutProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImplementation implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final CheckoutProductRepository checkoutProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    private final PaymentMethodRepository paymentMethodRepository;


    public CheckoutDTO getCheckout() {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);

        return CheckoutMapper.INSTANCE.CheckoutToCheckoutDTO(checkout);
    }

    public void createCheckOut(CreateCheckoutDTO checkoutDTO)
    {
        //Validates the user
        User user = getUser(checkoutDTO.getUserID());
        Checkout getCheckout = checkoutRepository.findByUser(user);
        if (getCheckout != null)
        {
            throw new ResourceAlreadyExistException("This user already has a checkout");
        }
        //Creates the Checkout with user information
        Checkout checkout = Checkout.builder()
                .user(user)
                .address(user.getAddress().isEmpty() ? null : user.getAddress().get(0))
                .paymentMethod(user.getPaymentMethods().isEmpty() ? null : user.getPaymentMethods().get(0))
                .build();
        Checkout savedCheckout = checkoutRepository.save(checkout);

        //Creates the specific checkout products with dto list information
        for (int i= 0; i < checkoutDTO.getProducts().size();i++)
        {
            Product getProduct = getProduct(checkoutDTO.getProducts().get(i).getProductName());
            createCheckoutProduct(getProduct,checkoutDTO.getProducts().get(i).getQuantity(),savedCheckout);
        }
    }

    public void addProductToCheckout(CheckoutProductDTO checkoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        Product getProduct = getProduct(checkoutProductDTO.getProductName());

        //Trying to find a checkout product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        //If it doesnt exist we create a new one
        if (checkoutProduct == null)
        {
            createCheckoutProduct(getProduct,checkoutProductDTO.getQuantity(),checkout);
            return;
        }
        //if it exist we validate and set the quantity
        setCheckoutProductQuantity(checkoutProduct,checkoutProduct.getQuantity() +
                checkoutProductDTO.getQuantity(),getProduct.getStock());
    }

    public void modifyProductQuantity(String productName, UpdateCheckoutProductDTO updateCheckoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        Product getProduct = getProduct(productName);

        //Trying to find a checkout product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        if (checkoutProduct ==  null)
        {
            throw new ResourceNotFoundException("We could not find a product checkout with the given information");
        }

        setCheckoutProductQuantity(checkoutProduct, updateCheckoutProductDTO.getQuantity(),getProduct.getStock());
    }


    //Method used to create a checkout product
    private void createCheckoutProduct(Product product,int quantity, Checkout checkout)
    {
        CheckoutProduct checkoutProduct = CheckoutProduct.builder()
                .product(product)
                .quantity(quantity)
                .checkout(checkout)
                .build();
        if (checkoutProduct.getQuantity() > product.getStock())
        {
            throw new NotEnoughStockException("Not enough items on stock");
        }

        checkoutProductRepository.save(checkoutProduct);
    }

    public void deleteCheckoutProduct(String productName)
    {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        Product getProduct = getProduct(productName);

        //Trying to find a checkout product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        if (checkoutProduct ==  null)
        {
            throw new ResourceNotFoundException("We could not find a product checkout with the given information");
        }

        checkoutProductRepository.delete(checkoutProduct);

        if (checkout.getCheckoutProducts().size() <= 1)
        {
            checkoutRepository.delete(checkout);
        }

    }

    public void deleteCheckout()
    {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        checkoutProductRepository.deleteAllByCheckout(checkout);
        checkoutRepository.delete(checkout);
    }

    public void changeCheckoutAddress (long id)
    {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);

        Address address = addressRepository.findByUserAndId(user,id);

        if (address == null)
        {
            throw new ResourceNotFoundException("We could not find an address with this id in this user");
        }

        checkout.setAddress(address);
        checkoutRepository.save(checkout);
    }

    public void changeCheckoutPaymentMethod (long id)
    {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);

        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndId(user,id);

        if (paymentMethod == null)
        {
            throw new ResourceNotFoundException("We could not find a payment method with this id in this user");
        }

        checkout.setPaymentMethod(paymentMethod);
        checkoutRepository.save(checkout);
    }


    //Secondary Methods
    //Sets the products quantity on the checkout product
    private void setCheckoutProductQuantity(CheckoutProduct checkoutProduct, int quantity, int stock)
    {
        if (quantity > stock)
        {
            throw new NotEnoughStockException("Not enough items on stock");
        }
        checkoutProduct.setQuantity(quantity);
        checkoutProductRepository.save(checkoutProduct);
    }


    ////Validates the Objects we recieve
    private User getUser (long id)
    {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user.get();
    }

    private Product getProduct (String name)
    {
        Product getProduct = productRepository.findByName(name);
        if (getProduct == null)
        {
            throw new ResourceNotFoundException("We could not find a product with the given name");
        }
        return getProduct;
    }

    private Checkout getCheckout (User user)
    {
        Checkout checkout = checkoutRepository.findByUser(user);
        if (checkout == null)
        {
            throw new ResourceNotFoundException("We could not find a checkout with the given user, please create one");
        }
        return checkout;
    }

}
