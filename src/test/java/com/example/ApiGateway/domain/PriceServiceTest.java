package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
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

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {


    @InjectMocks
    PriceService priceService;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    DirectExchange directExchange;

    public static final String ROUTING_KEY = "routingKey";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field field = priceService.getClass()
                .getDeclaredField("priceServiceRoutingKey");
        field                        .setAccessible(true);
        field.set(priceService, ROUTING_KEY);
    }

    @Test
    void get_price_empty_response_message() {
        var priceRequest = getPriceRequest();
        when(directExchange.getName()).thenReturn("test");
        priceService.getPrice(priceRequest);

        verify(rabbitTemplate).sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class));
    }

    @Test
    void get_price_non_empty_response_message() {
        var priceRequest = getPriceRequest();
        var priceResponse = new PriceResponse()
                .setId(1)
                .setTotalPrice(5500);
        when(directExchange.getName()).thenReturn("test");
        when(rabbitTemplate.sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(priceResponse)).getBytes()));

        var receivedResponse = priceService.getPrice(priceRequest);

        assertThat(receivedResponse.getId()).isEqualTo(1);
        assertThat(receivedResponse.getTotalPrice()).isEqualTo(5500);
    }

    private PriceRequest getPriceRequest() {

        return new PriceRequest()
                .setId(0L)
                .setProduct(
                        new Product()
                                .setName("testProduct")
                                .setComponents(getListOfTestComponents())
                );
    }

    private List<ProductComponent> getListOfTestComponents() {

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
}
