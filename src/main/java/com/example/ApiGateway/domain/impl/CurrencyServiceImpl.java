package com.example.ApiGateway.domain.impl;

import com.example.ApiGateway.domain.CurrencyService;
import com.example.ApiGateway.entity.CurrencyRequest;
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

import static com.example.ApiGateway.entity.MessageType.CURRENCY_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("${routing-keys.currency-service}")
    private String currencyServiceRoutingKey;

    @Override
    public CurrencyRequest getCurrency(CurrencyRequest currencyRequest) {
        var message = new Message((new Gson().toJson(currencyRequest)).getBytes());
        message.getMessageProperties()
                .setType(CURRENCY_REQUEST.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                currencyServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            log.error("error while receiving CurrencyRequest, because received Message from Currency Service via rabbitmq is empty");
            throw new ErrorResponseException("couldn't receive CurrencyRequest");
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                CurrencyRequest.class
        );
    }

    private boolean receivedMessageIsError(Message receivedMessage) {
        return receivedMessage == null ||
                receivedMessage.getBody() == null ||
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8).equals("errorResponse");
    }
}
