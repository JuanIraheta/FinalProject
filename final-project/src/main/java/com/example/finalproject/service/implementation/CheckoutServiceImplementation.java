package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.*;
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


    @Override
    public CheckoutDTO getCheckout() {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates if the checkout exist before obtaining it
        Checkout checkout = getCheckout(user);

        //Mapping the checkout to its dto
        CheckoutDTO checkoutDTO = CheckoutMapper.INSTANCE.checkoutToCheckoutDTO(checkout);

        //Mapping each checkout product to its dto
        List<ProductCheckoutDTO> productCheckoutDTO = new ArrayList<>();
        for (CheckoutProduct checkoutProduct: checkout.getCheckoutProducts())
        {
            ProductCheckoutDTO dto = CheckoutMapper.INSTANCE.checkoutProductAndProductToProductCheckoutDTO
                    (checkoutProduct,checkoutProduct.getProduct());
            productCheckoutDTO.add(dto);
        }
        //Setting the list of product dtos to the checkout dto
        checkoutDTO.setCheckoutProducts(productCheckoutDTO);
        //Calculates a subtotal and set it to the checkout dto
        checkoutDTO.setSubTotal(calculateCheckoutSubtotal(checkout));
        return checkoutDTO;
    }

    @Override
    public void createCheckOut(CreateCheckoutDTO checkoutDTO)
    {
        //Find and validates the user
        User user = getUser(checkoutDTO.getUserID());

        //Find the checkout of the user
        Checkout getCheckout = checkoutRepository.findByUser(user);
        //The user can have only one checkout, if there is one throw exception
        if (getCheckout != null)
        {
            throw new ResourceAlreadyExistException("This user already has a checkout");
        }

        //If there is no checkout we create a new one and save it
        Checkout checkout = createCheckoutBasedOnUser(user);
        Checkout savedCheckout = checkoutRepository.save(checkout);

        //Creates the specific checkout products with dto list information
        for (int i= 0; i < checkoutDTO.getProducts().size();i++)
        {
            //Obtains the specific product and validates its existence
            Product getProduct = getProduct(checkoutDTO.getProducts().get(i).getProductName());
            //Validates if there is enough stock and creates the checkout product
            createCheckoutProduct(getProduct,checkoutDTO.getProducts().get(i).getQuantity(),savedCheckout);
        }

    }

    @Override
    public void addProductToCheckout(CheckoutProductDTO checkoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);

        //If checkout doesn't exists it creates one
        Checkout checkout = checkoutRepository.findByUser(user);
        if (checkout == null)
        {
            checkout = createCheckoutBasedOnUser(user);
            checkout = checkoutRepository.save(checkout);
        }

        //Obtaining the specific product
        Product getProduct = getProduct(checkoutProductDTO.getProductName());

        //Trying to find a checkout product with this product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        //If it doesn't exist we create a new one
        if (checkoutProduct == null)
        {
            //Validates if there is enough stock and creates the checkout product
            createCheckoutProduct(getProduct,checkoutProductDTO.getQuantity(),checkout);
            return;
        }
        //if exist we validate that there is enough stock and set the quantity
        setCheckoutProductQuantity(checkoutProduct,checkoutProduct.getQuantity() +
                checkoutProductDTO.getQuantity(),getProduct.getStock());
    }


    @Override
    public void modifyCheckoutProductQuantity(String productName, UpdateCheckoutProductDTO updateCheckoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Validates that the product exist before obtaining it
        Product getProduct = getProduct(productName);

        //Validates that the checkout product exist before obtaining it
        CheckoutProduct checkoutProduct = getCheckoutProduct(checkout,getProduct);

        //Method that sets the quantity of the product checkout
        setCheckoutProductQuantity(checkoutProduct, checkoutProduct.getQuantity() +
                updateCheckoutProductDTO.getQuantity(), getProduct.getStock());

        //Delete the product from checkout if quantity is zero
        deleteCheckoutProductWhenQuantityZero(checkoutProduct);
        //Delete Checkout when there are no products
        deleteCheckoutNoProducts(checkout);
    }


    @Override
    public void deleteCheckoutProduct(String productName)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Validates that the product exist before obtaining it
        Product getProduct = getProduct(productName);

        //Trying to find a checkout product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        //If it doesnt find it then will throw an exception
        if (checkoutProduct ==  null)
        {
            throw new ResourceNotFoundException("We could not find this product in the checkout with the given information");
        }

        // Erase the product
        checkoutProductRepository.delete(checkoutProduct);

        //If there is no more products on this checkout then delete it
        deleteCheckoutNoProducts(checkout);
    }

    @Override
    public void deleteCheckout()
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Deletes all the products related to the checkout
//        checkoutProductRepository.deleteAllByCheckout(checkout);
        checkoutRepository.delete(checkout);
    }

    @Override
    public void changeCheckoutAddress (long id)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);

        //Find an address based on the user and the id of the address
        Address address = addressRepository.findByUserAndId(user,id);

        //If it doesn't find it throw an exception
        if (address == null)
        {
            throw new ResourceNotFoundException("We could not find an address with this id in this user");
        }

        //set the address of the checkout
        checkout.setAddress(address);
        //saves the modification to the database
        checkoutRepository.save(checkout);
    }

    @Override
    public void changeCheckoutPaymentMethod (long id)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);

        //Find a payment method based on the user and the id
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndId(user,id);

        //If it doesn't find one throw an exception
        if (paymentMethod == null)
        {
            throw new ResourceNotFoundException("We could not find a payment method with this id in this user");
        }

        //Sets the payment method of the checkout
        checkout.setPaymentMethod(paymentMethod);
        //saves the modification to the database
        checkoutRepository.save(checkout);
    }

    @Override
    public List<CheckoutUserAddressDTO> getAllAddresses()
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        //Find all the addresses related to the user
        List<Address> getAddresses = addressRepository.findAllByUser(user);
        if (getAddresses.isEmpty())
        {
            throw new ResourceNotFoundException("There are no addresses in this user, try to create one");
        }

        //Mapp the list of addresses to the dto
        List<CheckoutUserAddressDTO> addressDTO = new ArrayList<>();
        for (Address address: getAddresses)
        {
            CheckoutUserAddressDTO element = AddressMapper.INSTANCE.addressToCheckoutUserAddressDTO(address);
            addressDTO.add(element);
        }

        return addressDTO;
    }

    @Override
    public void createAddress(CreateAddressDTO createAddressDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        Address createAddress = AddressMapper.INSTANCE.createAddressDTOToAddress(createAddressDTO);
        createAddress.setUser(user);
        addressRepository.save(createAddress);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods()
    {
        //Get the specific user, its checkout and the product that is needed
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

    @Override
    public void createPaymentMethod(CreatePaymentMethodDTO createPaymentMethodDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        PaymentMethod createPaymentMethod = PaymentMethodMapper.INSTANCE.createPaymentMethodDTOToPaymentMethod(createPaymentMethodDTO);
        createPaymentMethod.setUser(user);
        paymentMethodRepository.save(createPaymentMethod);
    }

    @Override
    public void generateOrder()
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(1L);
        Checkout checkout = getCheckout(user);
        mandatoryCheckoutElementsValidation(checkout);
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

    private void deleteCheckoutProductWhenQuantityZero(CheckoutProduct checkoutProduct)
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

    private void mandatoryCheckoutElementsValidation(Checkout checkout)
    {
        if (checkout.getAddress() == null)
        {
            throw new RequiredInformationNullException("Address is mandatory, please set one to continue");
        }
        if (checkout.getPaymentMethod() == null)
        {
            throw new RequiredInformationNullException("PaymentMethod is mandatory, please set one to continue");
        }
    }

    private Checkout createCheckoutBasedOnUser (User user)
    {
        //Creates the Checkout with user information
        Checkout checkout = Checkout.builder()
                .user(user)
                .address(user.getAddress().isEmpty() ? null : user.getAddress().get(0))
                .paymentMethod(user.getPaymentMethods().isEmpty() ? null : user.getPaymentMethods().get(0))
                .build();

        return checkout;
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
