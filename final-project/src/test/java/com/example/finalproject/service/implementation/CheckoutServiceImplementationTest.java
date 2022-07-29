package com.example.finalproject.service.implementation;

import com.example.finalproject.exception.NotEnoughStockException;
import com.example.finalproject.exception.ResourceAlreadyExistException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.persistence.model.*;
import com.example.finalproject.persistence.repository.*;
import com.example.finalproject.web.DTO.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    class GetCheckout {
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
    class CreateCheckOut {
        @Test
        @DisplayName("createCheckOut When valid createCheckoutDTO create a checkout")
        void createCheckOut_ValidCreateCheckoutDTO_CreateCheckout()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.save(any())).thenReturn(checkoutProduct);

            checkoutServiceImplementation.createCheckOut("test@test.com",createCheckoutDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(checkoutRepository).save(any());
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).save(any());
        }
        @Test
        @DisplayName("createCheckOut When Null address and payment method create a checkout")
        void createCheckOut_NullAddressPaymentMethod_CreateCheckout()
        {
            User user = objectCreator.createUserNullElements();
            Checkout checkout = objectCreator.createCheckout();
            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.save(any())).thenReturn(checkoutProduct);

            checkoutServiceImplementation.createCheckOut("test@test.com",createCheckoutDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(checkoutRepository).save(any());
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).save(any());
        }

        @Test
        @DisplayName("createCheckOut When Existing checkout Throw Exception")
        void createCheckOut_CheckoutAlreadyExist_ThrowException()
        {
            User user = objectCreator.createUserNullElements();
            Checkout checkout = objectCreator.createCheckout();
            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);

            assertThrows(ResourceAlreadyExistException.class,() ->
                    checkoutServiceImplementation.createCheckOut("email@test.com",createCheckoutDTO));
            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
        }
        @Test
        @DisplayName("createCheckOut When Invalid Product Throw Exception")
        void createCheckOut_InvalidProduct_ThrowException()
        {
            User user = objectCreator.createUserNullElements();
            Checkout checkout = objectCreator.createCheckout();
            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();
            Optional<Product> optionalProduct = Optional.empty();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);

            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.createCheckOut("email@test.com",createCheckoutDTO));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(checkoutRepository).save(any());
            verify(productRepository).findById(any());
        }

        @Test
        @DisplayName("createCheckOut When Not Enough Stock Throw Exception")
        void createCheckOut_NotEnoughStock_ThrowException()
        {
            User user = objectCreator.createUserNullElements();
            Checkout checkout = objectCreator.createCheckout();
            CreateCheckoutDTO createCheckoutDTO = objectCreator.createCreateCheckoutDTO();
            createCheckoutDTO.getProducts().get(0).setQuantity(15);
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);



            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);


            assertThrows(NotEnoughStockException.class,() ->
                    checkoutServiceImplementation.createCheckOut("email@test.com",createCheckoutDTO));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(checkoutRepository).save(any());
            verify(productRepository).findById(any());

        }
    }

    @Nested
    @DisplayName("addProductToCheckout")
    class AddProductToCheckout {
        @Test
        @DisplayName("addProductToCheckout When CreateCheckoutProductDTO add a product to checkout")
        void addProductToCheckout_ValidCreateCheckoutProductDTO_AddProductToCheckout()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            CreateCheckoutProductDTO createCheckoutProductDTO = objectCreator.createCreateCheckoutProductDTO();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(null);
            when(checkoutProductRepository.save(any())).thenReturn(checkoutProduct);

            checkoutServiceImplementation.addProductToCheckout("test@test.com",createCheckoutProductDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
            verify(checkoutProductRepository).save(any());

        }

        @Test
        @DisplayName("addProductToCheckout When Checkout dont exist Create checkout with product")
        void addProductToCheckout_CheckoutDontExist_CreateCheckoutWithProduct()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            CreateCheckoutProductDTO createCheckoutProductDTO = objectCreator.createCreateCheckoutProductDTO();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(null);
            when(checkoutProductRepository.save(any())).thenReturn(checkoutProduct);

            checkoutServiceImplementation.addProductToCheckout("test@test.com",createCheckoutProductDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
            verify(checkoutProductRepository).save(any());
        }

        @Test
        @DisplayName("addProductToCheckout When Product Already in Checkout Add quantity")
        void addProductToCheckout_ProductAlreadyInCheckout_AddQuantityToProductCheckout()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            CreateCheckoutProductDTO createCheckoutProductDTO = objectCreator.createCreateCheckoutProductDTO();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);
            when(checkoutProductRepository.save(any())).thenReturn(checkoutProduct);


            checkoutServiceImplementation.addProductToCheckout("test@test.com",createCheckoutProductDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
            verify(checkoutProductRepository).save(any());

        }

        @Test
        @DisplayName("addProductToCheckout Not enough stock Throw Exception")
        void addProductToCheckout_NotEnoughStock_ThrowException()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            CreateCheckoutProductDTO createCheckoutProductDTO = objectCreator.createCreateCheckoutProductDTO();
            createCheckoutProductDTO.setQuantity(15);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(null);
            when(checkoutRepository.save(any())).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);
            assertThrows(NotEnoughStockException.class,() ->
                    checkoutServiceImplementation.addProductToCheckout("email@test.com",createCheckoutProductDTO));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
        }

    }

    @Nested
    @DisplayName("modifyCheckoutProductQuantity")
    class ModifyCheckoutProductQuantity {
        @Test
        @DisplayName("modifyCheckoutProductQuantity When ValidUpdateCheckoutDTO Add Product Quantity")
        void modifyCheckoutProductQuantity_ValidUpdateCheckoutDTO_AddProductQuantity()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            UpdateCheckoutProductDTO updateCheckoutProductDTO = objectCreator.createUpdateCheckoutProductDTO(1);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);

            checkoutServiceImplementation.modifyCheckoutProductQuantity("test@test.com",1L,updateCheckoutProductDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);

        }

        @Test
        @DisplayName("modifyCheckoutProductQuantity When ValidUpdateCheckoutDTO Add Product Quantity Product")
        void modifyCheckoutProductQuantity_ValidUpdateCheckoutDTO_RemoveProductQuantityAndProduct()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            checkoutProduct.setCheckout(checkout);
            UpdateCheckoutProductDTO updateCheckoutProductDTO = objectCreator.createUpdateCheckoutProductDTO(-1);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);


            checkoutServiceImplementation.modifyCheckoutProductQuantity("test@test.com",1L,updateCheckoutProductDTO);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);

        }

        @Test
        @DisplayName("modifyCheckoutProductQuantity When no checkout product Add Throw Exception")
        void modifyCheckoutProductQuantity_NoCheckoutProduct_ThrowException()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            UpdateCheckoutProductDTO updateCheckoutProductDTO = objectCreator.createUpdateCheckoutProductDTO(1);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.modifyCheckoutProductQuantity("test@test.com",1L,updateCheckoutProductDTO));
            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
        }
    }

    @Nested
    @DisplayName("deleteCheckoutProduct")
    class DeleteCheckoutProduct {
        @Test
        @DisplayName("deleteCheckoutProduct When valid user and id Delete Checkout Product")
        void deleteCheckoutProduct_ValidUserAndId_DeleteCheckoutProduct()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            checkoutProduct.setCheckout(checkout);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);

            checkoutServiceImplementation.deleteCheckoutProduct("test@test.com",1L);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
        }

        @Test
        @DisplayName("deleteCheckoutProduct When valid user and id Delete Checkout Product")
        void deleteCheckoutProduct_ValidUserAndId_DeleteCheckoutNoProducts()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            checkout.getCheckoutProducts().removeAll(checkout.getCheckoutProducts());
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);


            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(null);


            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.deleteCheckoutProduct("test@test.com",1L));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
        }

        @Test
        @DisplayName("deleteCheckoutProduct When invalid id Throw Exception")
        void deleteCheckoutProduct_InvalidId_DeleteCheckoutNoProducts()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            checkout.getCheckoutProducts().removeAll(checkout.getCheckoutProducts());
            Product product = objectCreator.createProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            CheckoutProduct checkoutProduct = objectCreator.createCheckoutProduct();
            checkoutProduct.setCheckout(checkout);

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(user)).thenReturn(checkout);
            when(productRepository.findById(any())).thenReturn(optionalProduct);
            when(checkoutProductRepository.findByCheckoutAndProduct(checkout,product)).thenReturn(checkoutProduct);
            doNothing().when(checkoutRepository).delete(checkout);

            checkoutServiceImplementation.deleteCheckoutProduct("test@test.com",1L);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(user);
            verify(productRepository).findById(any());
            verify(checkoutProductRepository).findByCheckoutAndProduct(checkout,product);
        }
    }

    @Test
    @DisplayName("deleteCheckout When valid user Delete Checkout")
    void deleteCheckout_ValidUser_DeleteCheckout()
    {
        User user = objectCreator.createUser();
        Checkout checkout = objectCreator.createCheckout();
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(checkoutRepository.findByUser(user)).thenReturn(checkout);
        doNothing().when(checkoutProductRepository).deleteAllByCheckout(checkout);
        doNothing().when(checkoutRepository).delete(checkout);

        checkoutServiceImplementation.deleteCheckout("test@test.com");
    }

    @Nested
    @DisplayName("changeCheckoutAddress")
    class ChangeCheckoutAddress {
        @Test
        @DisplayName("changeCheckoutAddress When valid Id Change Checkout Address")
        void changeCheckoutAddress_ValidID_ChangeCheckoutAddress()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            Address address = objectCreator.createAddress();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(any())).thenReturn(checkout);
            when(addressRepository.findByUserAndId(any(),anyLong())).thenReturn(address);

            checkoutServiceImplementation.changeCheckoutAddress("test@test.com", 1L);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(any());
            verify(addressRepository).findByUserAndId(any(),anyLong());
        }

        @Test
        @DisplayName("changeCheckoutAddress When no address Throw Exception")
        void changeCheckoutAddress_InvalidID_ThrowException()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(any())).thenReturn(checkout);
            when(addressRepository.findByUserAndId(any(),anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.changeCheckoutAddress("test@test.com",1L));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(any());
            verify(addressRepository).findByUserAndId(any(),anyLong());
        }
    }

    @Nested
    @DisplayName("changeCheckoutPaymentMethod")
    class ChangeCheckoutPaymentMethod {
        @Test
        @DisplayName("changeCheckoutAddress When valid Id Change Checkout Payment Method")
        void changeCheckoutPaymentMethod_ValidID_ChangeCheckoutPaymentMethod()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();
            PaymentMethod paymentMethod = objectCreator.createPaymentMethod();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(any())).thenReturn(checkout);
            when(paymentMethodRepository.findByUserAndId(any(),anyLong())).thenReturn(paymentMethod);

            checkoutServiceImplementation.changeCheckoutPaymentMethod("test@test.com", 1L);

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(any());
            verify(paymentMethodRepository).findByUserAndId(any(),anyLong());
        }

        @Test
        @DisplayName("changeCheckoutAddress When no payment method Throw Exception")
        void changeCheckoutAddress_InvalidId_ThrowException()
        {
            User user = objectCreator.createUser();
            Checkout checkout = objectCreator.createCheckout();

            when(userRepository.findByEmail(anyString())).thenReturn(user);
            when(checkoutRepository.findByUser(any())).thenReturn(checkout);
            when(paymentMethodRepository.findByUserAndId(any(),anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class,() ->
                    checkoutServiceImplementation.changeCheckoutPaymentMethod("test@test.com",1L));

            verify(userRepository).findByEmail(anyString());
            verify(checkoutRepository).findByUser(any());
            verify(paymentMethodRepository).findByUserAndId(any(),anyLong());
        }
    }
}