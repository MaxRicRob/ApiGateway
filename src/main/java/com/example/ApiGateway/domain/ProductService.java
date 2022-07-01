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

    @Value("${routing-keys.product-service}")
    private String productServiceRoutingKey;



    public List<ProductComponent> getAllComponents() {

        var receivedMessage =  rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                new Message("getComponents_x".getBytes())
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
                productServiceRoutingKey,
                new Message("getDefaultProducts_x".getBytes())
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
                productServiceRoutingKey,
                new Message(("getProductsFromUser_"+ userName).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while receiving Products from ProductService");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<Product>>(){}.getType()
        );


    }

    public Product deleteProduct(String id) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                new Message(("deleteProduct_"+ id).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while deleting Product from ProductService");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    public Product createProduct(Product product) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                new Message(("createProduct_"+ new Gson().toJson(product)).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while creating Product from ProductService");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    public Product updateProduct(Product product) {
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                new Message(("updateProduct_"+ new Gson().toJson(product)).getBytes())
        );
        if (receivedMessage == null) {
            log.error("error while updating Product from ProductService");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }
}
