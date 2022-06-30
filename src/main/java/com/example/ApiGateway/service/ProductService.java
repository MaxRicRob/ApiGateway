package com.example.ApiGateway.service;


import com.example.ApiGateway.api.dto.ProductComponent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("${routing-keys.product-service}")
    private String routingKeyProductService;


    public List<ProductComponent> getAllComponents() {

        Message m =  rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKeyProductService,
                new Message("getProductComponent".getBytes())
        );
        Type founderListType = new TypeToken<List<ProductComponent>>(){}.getType();
        String json = new String(m.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(json, founderListType);
    }


}
