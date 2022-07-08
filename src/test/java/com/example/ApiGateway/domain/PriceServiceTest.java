package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {


    @InjectMocks
    private PriceService priceService;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    public static final String ROUTING_KEY = "routingKey";

    @BeforeEach
    void setUp() {
        try {
            var field = priceService.getClass().getDeclaredField("priceServiceRoutingKey");
            field.setAccessible(true);
            field.set(priceService, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
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
                .setTotalPrice(5000);
        when(directExchange.getName()).thenReturn("test");
        when(rabbitTemplate.sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(priceResponse)).getBytes()));

        var receivedResponse = priceService.getPrice(priceRequest);

        assertThat(receivedResponse.getTotalPrice()).isEqualTo(5000);
    }

    private PriceRequest getPriceRequest() {

        return new PriceRequest()
                .setPrices(List.of(500L, 1500L , 3000L));
    }
}
