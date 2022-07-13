package com.example.ApiGateway.domain;

import com.example.ApiGateway.error.ErrorResponseException;
import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    public static final String TEST = "test";
    public static final String TEST_PRODUCT = "testProduct";
    public static final String TEST_USER = "testUser";
    @InjectMocks
    private ProductService productService;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    public static final String ROUTING_KEY = "routingKey";


    @BeforeEach
    void setUp() {
        try {
            var field = productService.getClass().getDeclaredField("productServiceRoutingKey");
            field.setAccessible(true);
            field.set(productService, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    void get_all_components_empty_response_message() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.getAllComponents());

    }

    @Test
    void get_all_components_non_empty_response_message() {
        var components = getTestComponents();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(components)).getBytes()));

        var receivedResponse = productService.getAllComponents();

        assertThat(receivedResponse.get(0).getName()).isEqualTo("Pineapple");
        assertThat(receivedResponse.get(1).getAwesomeness()).isEqualTo(7);
        assertThat(receivedResponse.get(2).getOrigin()).isEqualTo("France");
        assertThat(receivedResponse.size()).isEqualTo(3);
    }

    @Test
    void get_default_products_empty_response_message() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.getDefaultProducts());

    }

    @Test
    void get_default_products_non_empty_response_message() {
        var defaultProducts = getDefaultProducts();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(defaultProducts)).getBytes()));

        var receivedResponse = productService.getDefaultProducts();

        assertThat(receivedResponse.get(0).getName()).isEqualTo(TEST_PRODUCT);
        assertThat(receivedResponse.get(0).getId()).isEqualTo(0);
        assertThat(receivedResponse.get(0).getComponents().size()).isEqualTo(3);
    }

    @Test
    void get_products_from_user_empty_response_message() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.getProductsFromUser(TEST_USER));
    }

    @Test
    void get_products_from_user_non_empty_response_message() {
        var userProducts = getUserProducts();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(userProducts)).getBytes()));

        var receivedResponse = productService.getProductsFromUser(TEST_USER);

        assertThat(receivedResponse.get(0).getName()).isEqualTo(TEST_PRODUCT);
        assertThat(receivedResponse.get(0).getUserName()).isEqualTo(TEST_USER);
        assertThat(receivedResponse.get(0).getComponents().size()).isEqualTo(3);
    }

    @Test
    void delete_product_empty_response_message() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.deleteProduct("0"));
    }

    @Test
    void delete_product_non_empty_response_message() {
        var userProduct = getTestProduct();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(userProduct)).getBytes()));

        var receivedResponse = productService.deleteProduct("0");

        assertThat(receivedResponse.getName()).isEqualTo(TEST_PRODUCT);
        assertThat(receivedResponse.getUserName()).isEqualTo(TEST_USER);
        assertThat(receivedResponse.getComponents().size()).isEqualTo(3);
    }

    @Test
    void create_product_empty_message_response() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.createProduct(getTestProduct()));
    }

    @Test
    void create_product_non_empty_message_response() {
        var userProduct = getTestProduct();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(userProduct)).getBytes()));

        var receivedResponse = productService.createProduct(userProduct);

        assertThat(receivedResponse.getName()).isEqualTo(TEST_PRODUCT);
        assertThat(receivedResponse.getUserName()).isEqualTo(TEST_USER);
        assertThat(receivedResponse.getComponents().size()).isEqualTo(3);
    }

    @Test
    void update_product_empty_response_message() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productService.updateProduct(getTestProduct()));
    }

    @Test
    void update_product_non_empty_response_message() {
        var userProduct = getTestProduct();
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(userProduct)).getBytes()));

        var receivedResponse = productService.updateProduct(userProduct);

        assertThat(receivedResponse.getName()).isEqualTo(TEST_PRODUCT);
        assertThat(receivedResponse.getUserName()).isEqualTo(TEST_USER);
        assertThat(receivedResponse.getComponents().size()).isEqualTo(3);

    }

    private List<ProductComponent> getTestComponents() {

        return List.of(
                new ProductComponent()
                        .setId(0)
                        .setName("Pineapple")
                        .setPrice(1700)
                        .setWeight(0)
                        .setColor("Yellow")
                        .setOrigin("Mexico")
                        .setAwesomeness(9)
                        .setFarmer("Alice")
                        .setOrganic(true)
                        .setCalories(50),
                new ProductComponent()
                        .setId(0)
                        .setName("Banana")
                        .setPrice(2300)
                        .setWeight(9)
                        .setColor("Yellow")
                        .setOrigin("Brazil")
                        .setAwesomeness(7)
                        .setFarmer("Bob")
                        .setOrganic(false)
                        .setCalories(88),
                new ProductComponent()
                        .setId(0)
                        .setName("Apple")
                        .setPrice(1500)
                        .setWeight(8)
                        .setColor("Red")
                        .setOrigin("France")
                        .setAwesomeness(6)
                        .setFarmer("Charlie")
                        .setOrganic(true)
                        .setCalories(52)
        );
    }

    private List<DefaultProduct> getDefaultProducts() {
        return List.of(
                new DefaultProduct()
                        .setName(TEST_PRODUCT)
                        .setId(0)
                        .setComponents(getTestComponents())
        );
    }

    private List<Product> getUserProducts() {
        return List.of(
                getTestProduct()
        );
    }

    private Product getTestProduct() {
        return new Product()
                .setName(TEST_PRODUCT)
                .setUserName(TEST_USER)
                .setId(UUID.randomUUID())
                .setComponents(getTestComponents());
    }
}
