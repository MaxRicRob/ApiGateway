package com.example.ApiGateway.domain;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("${routing-keys.components}")
    private String componentsRoutingKey;

    @Value("${routing-keys.default-products}")
    private String defaultProductsRoutingKey;

    @Value("${routing-keys.user-products}")
    private String userProductsRoutingKey;


    public List<ProductComponent> getAllComponents() {

        var receivedMessage =  rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                componentsRoutingKey,
                new Message("getProductComponent".getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while receiving productComponents from ProductService");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<ProductComponent>>(){}.getType()
        );
    }

    public List<DefaultProduct> getDefaultProducts() {

        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                defaultProductsRoutingKey,
                new Message("getDefaultProducts".getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while receiving defaultProducts from ProductService");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<DefaultProduct>>(){}.getType()
        );
    }


    public List<Product> getProductsFromUser(String userName) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                userProductsRoutingKey,
                new Message(userName.getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while receiving userProducts from ProductService");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<Product>>(){}.getType()
        );


    }
}
