package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyResponse;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("${routing-keys.currency-service}")
    private String currencyServiceRoutingKey;

    public CurrencyResponse getCurrency(CurrencyRequest currencyRequest) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                currencyServiceRoutingKey,
                new Message(("currencyRequest_" + new Gson().toJson(currencyRequest)).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while receiving CurrencyResponse, because received Message from Currency Service via rabbitmq is empty");
            return new CurrencyResponse();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                CurrencyResponse.class
        );
    }
}
