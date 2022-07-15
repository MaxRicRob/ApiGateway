package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.CurrencyRequest;
import com.example.ApiGateway.domain.impl.CurrencyServiceImpl;
import com.example.ApiGateway.error.ErrorResponseException;
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

import static com.example.ApiGateway.domain.entity.Currency.MXN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {


    public static final String ROUTING_KEY = "routingKey";
    @InjectMocks
    private CurrencyServiceImpl currencyServiceImpl;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    @BeforeEach
    void setUp() {
        try {
            var field = currencyServiceImpl.getClass().getDeclaredField("currencyServiceRoutingKey");
            field.setAccessible(true);
            field.set(currencyServiceImpl, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    void get_Currency_empty_response_message() {
        var currencyRequest = new CurrencyRequest()
                .setWantedCurrency(MXN);
        when(directExchange.getName()).thenReturn("test");

        assertThrows(ErrorResponseException.class, () ->
                currencyServiceImpl.getCurrency(currencyRequest));

    }

    @Test
    void get_currency_non_empty_response_message() {
        var currencyRequest = new CurrencyRequest()
                .setWantedCurrency(MXN)
                .setTotalPrice(200);
        when(directExchange.getName()).thenReturn("test");
        when(rabbitTemplate.sendAndReceive(eq("test"), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(currencyRequest)).getBytes()));

        var receivedResponse = currencyServiceImpl.getCurrency(currencyRequest);

        assertThat(receivedResponse.getWantedCurrency()).isEqualTo(MXN);
        assertThat(receivedResponse.getTotalPrice()).isEqualTo(200);
    }
}
