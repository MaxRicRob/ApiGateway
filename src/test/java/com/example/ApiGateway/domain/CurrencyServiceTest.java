package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.CurrencyResponse;
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

import static com.example.ApiGateway.entity.Currency.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {


    @InjectMocks
    CurrencyService currencyService;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    DirectExchange directExchange;

    public static final String ROUTING_KEY = "routingKey";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field field = currencyService.getClass()
                .getDeclaredField("currencyServiceRoutingKey");
        field                        .setAccessible(true);
        field.set(currencyService, ROUTING_KEY);
    }

    @Test
    void get_Currency_empty_response_message() {
        var currencyRequest = new CurrencyRequest()
                .setWantedCurrency(MXN)
                .setId(1);
        when(directExchange.getName()).thenReturn("test");
        currencyService.getCurrency(currencyRequest);

        verify(rabbitTemplate).sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class));
    }

    @Test
    void get_currency_non_empty_response_message() {
        var currencyRequest = new CurrencyRequest()
                .setWantedCurrency(MXN)
                .setId(1);
        var currencyResponse = new CurrencyResponse()
                .setId(1)
                .setUpdatedCurrency(MXN)
                .setUpdatedPrice(200);
        when(directExchange.getName()).thenReturn("test");
        when(rabbitTemplate.sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(currencyResponse)).getBytes()));

        var receivedResponse = currencyService.getCurrency(currencyRequest);

        assertThat(receivedResponse.getId()).isEqualTo(1);
        assertThat(receivedResponse.getUpdatedCurrency()).isEqualTo(MXN);
        assertThat(receivedResponse.getUpdatedPrice()).isEqualTo(200);
    }
}
