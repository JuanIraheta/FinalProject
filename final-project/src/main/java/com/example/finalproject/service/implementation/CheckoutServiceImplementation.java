package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.NotEnoughFoundsException;
import com.example.finalproject.exception.NotEnoughStockException;
import com.example.finalproject.exception.ResourceAlreadyExistException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.*;
import com.example.finalproject.persistence.repository.*;
import com.example.finalproject.service.CheckoutService;
import com.example.finalproject.service.mapper.AddressMapper;
import com.example.finalproject.service.mapper.CheckoutMapper;
import com.example.finalproject.service.mapper.CheckoutOrderMapper;
import com.example.finalproject.service.mapper.PaymentMethodMapper;
import com.example.finalproject.web.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final TransactionRepository transactionRepository;


    public CheckoutDTO getCheckout() {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);

        CheckoutDTO checkoutDTO = CheckoutMapper.INSTANCE.checkoutToCheckoutDTO(checkout);
        checkoutDTO.setSubTotal(calculateCheckoutSubtotal(checkout));
        return checkoutDTO;
    }

    public void createCheckOut(CreateCheckoutDTO checkoutDTO)
    {
        //Validates the user
        User user = getUser(checkoutDTO.getUserID());
        Checkout getCheckout = checkoutRepository.findByUser(user);
        //The user can have only one checkout
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
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        Product getProduct = getProduct(productName);

        CheckoutProduct checkoutProduct = getCheckoutProduct(checkout,getProduct);

        //Method modifies the quanitty of the product
        setCheckoutProductQuantity(checkoutProduct, checkoutProduct.getQuantity() +
                updateCheckoutProductDTO.getQuantity(), getProduct.getStock());

        //Delete the product from checkout if quantity is zero
        deleteCheckoutProductQuantityZero(checkoutProduct);
        //Delete Checkout when there are no products
        deleteCheckoutNoProducts(checkout);
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

        deleteCheckoutNoProducts(checkout);
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

    public List<CheckoutUserAddressDTO> getAllAddresses()
    {
        User user = getUser(1L);
        List<Address> getAddresses = addressRepository.findAllByUser(user);
        if (getAddresses.isEmpty())
        {
            throw new ResourceNotFoundException("There are no addresses in this user, try to create one");
        }

        List<CheckoutUserAddressDTO> addressDTO = new ArrayList<>();

        for (Address address: getAddresses)
        {
            CheckoutUserAddressDTO element = AddressMapper.INSTANCE.addressToCheckoutUserAddressDTO(address);
            addressDTO.add(element);
        }

        return addressDTO;
    }

    public void createAddress(CreateAddressDTO createAddressDTO)
    {
        User user = getUser(1L);
        Address createAddress = AddressMapper.INSTANCE.createAddressDTOToAddress(createAddressDTO);
        createAddress.setUser(user);
        addressRepository.save(createAddress);
    }

    public List<PaymentMethodDTO> getAllPaymentMethods()
    {
        User user = getUser(1L);
        List<PaymentMethod> getPaymentMethods = paymentMethodRepository.findAllByUser(user);
        if (getPaymentMethods.isEmpty())
        {
            throw new ResourceNotFoundException("There are no payment methods in this user, try to create one");
        }
        List<PaymentMethodDTO> paymentMethodDTO = new ArrayList<>();

        for (PaymentMethod paymentMethod: getPaymentMethods)
        {
            PaymentMethodDTO element = PaymentMethodMapper.INSTANCE.paymentMethodToPaymentMethodDTO(paymentMethod);
            paymentMethodDTO.add(element);
        }

        return paymentMethodDTO;
    }

    public void createPaymentMethod(CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        User user = getUser(1L);
        PaymentMethod createPaymentMethod = PaymentMethodMapper.INSTANCE.createPaymentMethodDTOToPaymentMethod(createPaymentMethodDTO);
        createPaymentMethod.setUser(user);
        paymentMethodRepository.save(createPaymentMethod);
    }

    public void generateOrder()
    {
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        //Generate basic order
        Orders order = createOrderBasedOnCheckout(checkout);

        //Create all de order products and getting the total price
        double total = createOrderProductsCalculateTotal(checkout,order);
        order.setTotal(total);

        //Generate transaction
        Transaction transaction = generateTransaction(order);
        order.setTransaction(transaction);

        //Remove founds from payment method
        order.getPaymentMethod().setFounds(order.getPaymentMethod().getFounds() - transaction.getQuantity());

        //Saving the order with all the elements and deleting the checkout
        orderRepository.save(order);
        deleteCheckout();
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

    private void deleteCheckoutProductQuantityZero(CheckoutProduct checkoutProduct)
    {
        if (checkoutProduct.getQuantity() <= 0)
        {
            checkoutProductRepository.delete(checkoutProduct);
        }
    }

    private void deleteCheckoutNoProducts(Checkout checkout)
    {
        if (checkout.getCheckoutProducts().size() <= 1)
        {
            checkoutRepository.delete(checkout);
        }
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

    private double calculateCheckoutSubtotal(Checkout checkout)
    {
        double subTotal = 0;
        for (int i = 0; i < checkout.getCheckoutProducts().size(); i++)
        {
            CheckoutProduct product = checkout.getCheckoutProducts().get(i);
            subTotal += product.getProduct().getPrice() * product.getQuantity();
        }

        return subTotal;
    }

    private double createOrderProductsCalculateTotal(Checkout checkout, Orders order)
    {
        double total = 0;
        // Generate Order Products
        for (int i= 0; i < checkout.getCheckoutProducts().size();i++)
        {
            OrderProduct orderProduct = CheckoutOrderMapper.INSTANCE.checkoutProductToOrderProduct(checkout.getCheckoutProducts().get(i));
            orderProduct.setOrder(order);

            discountStockFromProducts(orderProduct);
            //Getting total
            total += orderProduct.getProduct().getPrice() * orderProduct.getQuantity();

            orderProductRepository.save(orderProduct);
        }

        return total;
    }

    private void discountStockFromProducts(OrderProduct orderProduct)
    {
        //Eliminating stocks from product
        orderProduct.getProduct().setStock(orderProduct.getProduct().getStock() - orderProduct.getQuantity());
        productRepository.save(orderProduct.getProduct());
    }

    private Transaction generateTransaction (Orders order)
    {
        //Generate Transaction
        Transaction transaction = Transaction.builder()
                .paymentMethod(order.getPaymentMethod())
                .quantity(order.getTotal())
                .build();
        return transactionRepository.save(transaction);
    }

    private Orders createOrderBasedOnCheckout (Checkout checkout)
    {
        Orders createOrder = CheckoutOrderMapper.INSTANCE.checkoutToOrder(checkout);

        if (calculateCheckoutSubtotal(checkout) > createOrder.getPaymentMethod().getFounds())
        {
            throw new NotEnoughFoundsException("Not enough founds on your payment method");
        }

        //Generate basic order
        return orderRepository.save(createOrder);
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

    private CheckoutProduct getCheckoutProduct(Checkout checkout, Product product)
    {
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,product);
        if (checkoutProduct ==  null)
        {
            throw new ResourceNotFoundException("We could not find a product checkout with the given information");
        }

        return checkoutProduct;
    }


}
