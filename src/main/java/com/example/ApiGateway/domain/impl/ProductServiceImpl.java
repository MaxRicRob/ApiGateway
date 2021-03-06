package com.example.ApiGateway.domain.impl;


import com.example.ApiGateway.domain.ProductService;
import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
import com.example.ApiGateway.error.ErrorResponseException;
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
import java.util.List;

import static com.example.ApiGateway.entity.MessageType.CREATE_PRODUCT;
import static com.example.ApiGateway.entity.MessageType.DELETE_PRODUCT;
import static com.example.ApiGateway.entity.MessageType.GET_COMPONENTS;
import static com.example.ApiGateway.entity.MessageType.GET_DEFAULT_PRODUCTS;
import static com.example.ApiGateway.entity.MessageType.GET_PRODUCTS_FROM_USER;
import static com.example.ApiGateway.entity.MessageType.UPDATE_PRODUCT;

@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;
    @Value("${routing-keys.product-service}")
    private String productServiceRoutingKey;

    @Override
    public List<ProductComponent> getAllComponents() {

        var message = new Message("".getBytes());
        setMessageType(message, GET_COMPONENTS.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("receiving productComponents");
            throw new ErrorResponseException("couldn't receive components");
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<ProductComponent>>() {
                }.getType()
        );
    }

    @Override
    public List<DefaultProduct> getDefaultProducts() {

        var message = new Message("".getBytes());
        setMessageType(message, GET_DEFAULT_PRODUCTS.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("receiving defaultProducts");
            throw new ErrorResponseException("couldn't receive default products");
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<DefaultProduct>>() {
                }.getType()
        );
    }


    @Override
    public List<Product> getProductsFromUser(String userName) {

        var message = new Message(userName.getBytes());
        setMessageType(message, GET_PRODUCTS_FROM_USER.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("receiving Products");
            throw new ErrorResponseException("couldn't receive products from user: " + userName);
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<Product>>() {
                }.getType()
        );
    }

    @Override
    public String deleteProduct(String id) {

        var message = new Message(id.getBytes());
        setMessageType(message, DELETE_PRODUCT.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("deleting Product");
            throw new ErrorResponseException("couldn't delete product");
        }
        return new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
    }

    @Override
    public Product createProduct(Product product) {

        var message = new Message(new Gson().toJson(product).getBytes());
        setMessageType(message, CREATE_PRODUCT.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("creating Product");
            throw new ErrorResponseException("couldn't create product");

        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                Product.class
        );
    }

    @Override
    public Product updateProduct(Product product) {
        var message = new Message(new Gson().toJson(product).getBytes());
        setMessageType(message, UPDATE_PRODUCT.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                productServiceRoutingKey,
                message
        );
        if (receivedMessageIsError(receivedMessage)) {
            trackErrorFor("updating Product");
            throw new ErrorResponseException("couldn't update product");
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

    private boolean receivedMessageIsError(Message receivedMessage) {
        return receivedMessage == null ||
                receivedMessage.getBody() == null ||
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8).equals("errorResponse");
    }


    private void trackErrorFor(String taskName) {
        log.error("error while '{}', because received Message from Product Service via rabbitmq is empty", taskName);
    }
}
