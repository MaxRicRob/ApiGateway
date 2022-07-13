package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;
import com.example.ApiGateway.error.ErrorResponseException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

import static com.example.ApiGateway.domain.MessageType.PRICE_REQUEST;

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
        var message = new Message((new Gson().toJson(priceRequest)).getBytes());
        message.getMessageProperties()
                .setType(PRICE_REQUEST.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                priceServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            log.error("error while receiving PriceResponse, because received Message from Price Service via rabbitmq is empty");
            throw new ErrorResponseException("couldn't receive priceRequest");
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                PriceResponse.class
        );
    }

    private boolean receivedMessageIsError(Message receivedMessage) {
        return receivedMessage == null ||
                receivedMessage.getBody() == null ||
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8).equals("errorResponse");
    }
}
