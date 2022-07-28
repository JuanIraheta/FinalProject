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
    public CheckoutDTO getCheckout(String email) {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
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
    public void createCheckOut(String email,CreateCheckoutDTO checkoutDTO)
    {
        //Find and validates the user
        User user = getUser(email);

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
            Product getProduct = getProduct(checkoutDTO.getProducts().get(i).getId());
            //Validates if there is enough stock and creates the checkout product
            createCheckoutProduct(getProduct,checkoutDTO.getProducts().get(i).getQuantity(),savedCheckout);
        }

    }

    @Override
    public void addProductToCheckout(String email,CheckoutProductDTO checkoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);

        //If checkout doesn't exists it creates one
        Checkout checkout = checkoutRepository.findByUser(user);
        if (checkout == null)
        {
            checkout = createCheckoutBasedOnUser(user);
            checkout = checkoutRepository.save(checkout);
        }

        //Obtaining the specific product
        Product getProduct = getProduct(checkoutProductDTO.getId());

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
    public void modifyCheckoutProductQuantity(String email,long productID, UpdateCheckoutProductDTO updateCheckoutProductDTO)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Validates that the product exist before obtaining it
        Product getProduct = getProduct(productID);

        //Validates that the checkout product exist before obtaining it
        CheckoutProduct checkoutProduct = getCheckoutProduct(checkout,getProduct);

        //Method modifies the quanitty of the product
        setCheckoutProductQuantity(checkoutProduct, checkoutProduct.getQuantity() +
                updateCheckoutProductDTO.getQuantity(), getProduct.getStock());

        //Delete the product from checkout if quantity is zero
        deleteCheckoutProductWhenQuantityZero(checkoutProduct);
        //Delete Checkout when there are no products
        deleteCheckoutNoProducts(checkout);
    }


    @Override
    public void deleteCheckoutProduct(String email,long productId)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Validates that the product exist before obtaining it
        Product getProduct = getProduct(productId);

        //Trying to find a checkout product
        CheckoutProduct checkoutProduct = checkoutProductRepository.findByCheckoutAndProduct(checkout,getProduct);
        //If it doesnt find it then will throw an exception
        if (checkoutProduct ==  null)
        {
            throw new ResourceNotFoundException("We could not find this product in the checkout with the given information");
        }

        // Erase the product
        checkoutProductDeletion(checkoutProduct);

        //If there is no more products on this checkout then delete it
        deleteCheckoutNoProducts(checkout);
    }


    @Override
    public void deleteCheckout(String email)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);

        checkoutProductRepository.deleteAllByCheckout(checkout);
        checkoutRepository.delete(checkout);
    }

    @Override
    public void changeCheckoutAddress (String email,long id)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
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
    public void changeCheckoutPaymentMethod (String email,long id)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
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
    public void generateOrder(String email)
    {
        //Get the specific user, its checkout and the product that is needed
        User user = getUser(email);
        //Validates the checkout before obtaining it by user
        Checkout checkout = getCheckout(user);
        //Verify if the address and payment of the checkout are not null
        mandatoryCheckoutElementsValidation(checkout);
        //Generate basic order
        Orders order = createOrderBasedOnCheckout(checkout);

        //Create all de order products and getting the total price
        double total = createOrderProductsCalculateTotal(checkout,order);
        order.setTotal(total);

        //Generate transaction based on the order created
        Transaction transaction = generateTransaction(order);
        order.setTransaction(transaction);

        //Remove founds from payment method
        setPaymentMethodFounds(order,transaction);

        //Saving the order with all the elements and deleting the checkout
        orderRepository.save(order);
        // deletes the checkout after the order is completed
        deleteCheckout(email);
    }



    //Secondary Methods
    //Sets the products quantity on the checkout product
    private void setCheckoutProductQuantity(CheckoutProduct checkoutProduct, int quantity, int stock)
    {
        if (quantity > stock)
        {
            throw new NotEnoughStockException("Not enough "+ checkoutProduct.getProduct().getName() +
                    " on stock - current stock: " + checkoutProduct.getProduct().getStock());
        }
        checkoutProduct.setQuantity(quantity);
        checkoutProductRepository.save(checkoutProduct);
    }

    //Deletes the product of the checkout when the quantity reaches zero
    private void deleteCheckoutProductWhenQuantityZero(CheckoutProduct checkoutProduct)
    {
        if (checkoutProduct.getQuantity() <= 0)
        {
            checkoutProductDeletion(checkoutProduct);
        }
    }

    //Method used to delete a checkout product
    private void checkoutProductDeletion(CheckoutProduct checkoutProduct)
    {
        checkoutProduct.getCheckout().getCheckoutProducts().remove(checkoutProduct);
        checkoutProductRepository.delete(checkoutProduct);
    }

    //Deletes the checkout if it doesn't have any more products
    private void deleteCheckoutNoProducts(Checkout checkout)
    {
        if (checkout.getCheckoutProducts().isEmpty())
        {
            checkoutRepository.delete(checkout);
        }
    }

    //Method used to create a checkout product and validates that the quantity is less than stock of product
    private void createCheckoutProduct(Product product,int quantity, Checkout checkout)
    {
        CheckoutProduct checkoutProduct = CheckoutProduct.builder()
                .product(product)
                .quantity(quantity)
                .checkout(checkout)
                .build();
        if (checkoutProduct.getQuantity() > product.getStock())
        {
            throw new NotEnoughStockException("Not enough "+ product.getName() + " on stock - current stock: " + product.getStock());
        }

        checkoutProductRepository.save(checkoutProduct);
    }

    //Calculates the subtotal of the current state of the checkout
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

    //Create the order products based on the checkout products, removes stock from products and returns total
    private double createOrderProductsCalculateTotal(Checkout checkout, Orders order)
    {
        double total = 0;
        // Generate Order Products
        for (int i= 0; i < checkout.getCheckoutProducts().size();i++)
        {
            OrderProduct orderProduct = CheckoutOrderMapper.INSTANCE.
                    checkoutProductToOrderProduct(checkout.getCheckoutProducts().get(i));
            orderProduct.setOrder(order);

            discountStockFromProducts(orderProduct);
            //Getting total
            total += orderProduct.getProduct().getPrice() * orderProduct.getQuantity();

            orderProductRepository.save(orderProduct);
        }

        return total;
    }

    //Removes stock from products after the order is generated
    private void discountStockFromProducts(OrderProduct orderProduct)
    {
        //Eliminating stocks from product
        if(orderProduct.getQuantity() > orderProduct.getProduct().getStock())
        {
            throw new NotEnoughStockException("Not enough "+ orderProduct.getProduct().getName() +
                    " on stock - current stock: " + orderProduct.getProduct().getStock());
        }
        orderProduct.getProduct().setStock(orderProduct.getProduct().getStock() - orderProduct.getQuantity());
        productRepository.save(orderProduct.getProduct());
    }

    //Creates the transaction based on the order
    private Transaction generateTransaction (Orders order)
    {
        //Generate Transaction
        Transaction transaction = Transaction.builder()
                .paymentMethod(order.getPaymentMethod())
                .quantity(order.getTotal())
                .build();
        return transactionRepository.save(transaction);
    }

    //Creates an order based on the user checkout
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

    //Validates that the address and the payment are not null when generating the order
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

    //Creates the checkout based on the user
    private Checkout createCheckoutBasedOnUser (User user)
    {
        //Creates the Checkout with user information
        return Checkout.builder()
                .user(user)
                .address(user.getAddress().isEmpty() ? null : user.getAddress().get(0))
                .paymentMethod(user.getPaymentMethods().isEmpty() ? null : user.getPaymentMethods().get(0))
                .build();
    }

    //Remove founds when generating the order, if there are not enough founds then throw exception
    private void setPaymentMethodFounds(Orders order, Transaction transaction)
    {
        if (transaction.getQuantity() > order.getPaymentMethod().getFounds())
        {
            throw new NotEnoughFoundsException("Not enough founds on your payment method");
        }
        order.getPaymentMethod().setFounds(order.getPaymentMethod().getFounds() - transaction.getQuantity());
    }





    ////Validates the Objects we recieve
    private User getUser (String email)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new ResourceNotFoundException("We could not find a user with the given id");
        }
        return user;
    }

    private Product getProduct (long id)
    {
        Optional<Product> getProduct = productRepository.findById(id);
        if (!getProduct.isPresent())
        {
            throw new ResourceNotFoundException("We could not find a product with the given name");
        }
        return getProduct.get();
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
