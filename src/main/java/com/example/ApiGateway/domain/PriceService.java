package com.example.ApiGateway.domain;

import com.example.ApiGateway.api.dto.PriceResponse;
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
public class PriceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("${routing-keys.price-service}")
    private String priceServiceRoutingKey;

    public PriceResponse getPrice(PriceRequest priceRequest) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                priceServiceRoutingKey,
                new Message(("priceRequest_"+ new Gson().toJson(priceRequest)).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while getting Price from PriceService");
            return new PriceResponse();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                PriceResponse.class
        );
    }
}
