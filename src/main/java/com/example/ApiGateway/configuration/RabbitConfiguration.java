package com.example.ApiGateway.configuration;


import com.example.ApiGateway.service.ProductService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfiguration {

    @Value("${xchange.name}")
    private String directXchangeName;

    @Value("${routing-keys.components}")
    private String componentsRoutingKey;

    @Value("${queue-names.components}")
    private String componentsQueueName;

    @Value("${routing-keys.default-products}")
    private String defaultProductsRoutingKey;

    @Value("${queue-names.default-products}")
    private String defaultProductsQueueName;

    @Bean
    public ProductService productService() {
        return new ProductService();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directXchangeName);
    }

    @Bean
    public Queue componentsQueue() {
        return new Queue(componentsQueueName);
    }

    @Bean
    public Binding componentsBinding(DirectExchange directExchange, Queue componentsQueue) {
        return BindingBuilder.bind(componentsQueue).to(directExchange).with(componentsRoutingKey);
    }
    @Bean
    public Queue defaultProductsQueue() {
        return new Queue(defaultProductsQueueName);
    }

    @Bean
    public Binding defaultProductsBinding(DirectExchange directExchange, Queue defaultProductsQueue) {
        return BindingBuilder.bind(defaultProductsQueue).to(directExchange).with(defaultProductsRoutingKey);
    }

}
