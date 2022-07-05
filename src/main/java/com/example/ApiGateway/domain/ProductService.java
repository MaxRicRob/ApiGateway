package com.example.ApiGateway.domain;


import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
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

        var message = new Message("".getBytes());
        setMessageHeader(message, "getComponents");
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("receiving productComponents");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<ProductComponent>>() {
                }.getType()
        );
    }

    public List<DefaultProduct> getDefaultProducts() {

        var message =  new Message("".getBytes());
        setMessageHeader(message, "getDefaultProducts");
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("receiving defaultProducts");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<DefaultProduct>>() {
                }.getType()
        );
    }


    public List<Product> getProductsFromUser(String userName) {

        var message = new Message(userName.getBytes());
        setMessageHeader(message, "getProductsFromUser");
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("receiving Products");
            return Collections.emptyList();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<Product>>() {
                }.getType()
        );
    }

    public Product deleteProduct(String id) {

        var message = new Message(id.getBytes());
        setMessageHeader(message,"deleteProduct" );
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("deleting Product");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    public Product createProduct(Product product) {

        var message = new Message(new Gson().toJson(product).getBytes());
        setMessageHeader(message,"createProduct");
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("creating Product");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    public Product updateProduct(Product product) {
        var message = new Message(new Gson().toJson(product).getBytes());
        setMessageHeader(message,"updateProduct");
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsEmpty(receivedMessage)) {
            trackErrorFor("updating Product");
            return new Product();
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    private void setMessageHeader(Message message, String value) {
        message.getMessageProperties()
                .setHeader("key", value);
    }

    private boolean receivedMessageIsEmpty(Message receivedMessage) {
        return receivedMessage == null;
    }

    private void trackErrorFor(String taskName) {
        log.error("error while '{}', because received Message from Product Service via rabbitmq is empty", taskName);
    }
}
