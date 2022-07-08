package com.example.ApiGateway.domain;


import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
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

import static com.example.ApiGateway.domain.MessageType.CREATE_PRODUCT;
import static com.example.ApiGateway.domain.MessageType.DELETE_PRODUCT;
import static com.example.ApiGateway.domain.MessageType.GET_COMPONENTS;
import static com.example.ApiGateway.domain.MessageType.GET_DEFAULT_PRODUCTS;
import static com.example.ApiGateway.domain.MessageType.GET_PRODUCTS_FROM_USER;
import static com.example.ApiGateway.domain.MessageType.UPDATE_PRODUCT;

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
        setMessageType(message, GET_COMPONENTS.name());
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
        setMessageType(message, GET_DEFAULT_PRODUCTS.name());
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
        setMessageType(message, GET_PRODUCTS_FROM_USER.name());
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
        setMessageType(message, DELETE_PRODUCT.name() );
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
        setMessageType(message, CREATE_PRODUCT.name());
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
        setMessageType(message, UPDATE_PRODUCT.name());
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

    private void setMessageType(Message message, String type) {

        message.getMessageProperties()
                .setType(type);
    }

    private boolean receivedMessageIsEmpty(Message receivedMessage) {

        return receivedMessage == null;
    }

    private void trackErrorFor(String taskName) {
        log.error("error while '{}', because received Message from Product Service via rabbitmq is empty", taskName);
    }
}
