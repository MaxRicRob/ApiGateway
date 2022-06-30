package com.example.ApiGateway.service;


import com.example.ApiGateway.api.dto.ProductComponent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Type;
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
    private String routingKeyProductService;


    public List<ProductComponent> getAllComponents() {

        Message m =  rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKeyProductService,
                new Message("getProductComponent".getBytes())
        );
        if (m == null) {
            log.error("error while receiving productComponents from ProductService");
            return Collections.emptyList();
        }
        String json = new String(m.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(json, new TypeToken<List<ProductComponent>>(){}.getType());
    }


}
