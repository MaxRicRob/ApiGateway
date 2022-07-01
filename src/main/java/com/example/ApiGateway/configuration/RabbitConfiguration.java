package com.example.ApiGateway.configuration;


import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.ProductService;
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

    @Value("${routing-keys.product-service}")
    private String productServiceRoutingKey;

    @Value("${queue-names.product-service}")
    private String productServiceQueueName;

    @Value("${routing-keys.price-service}")
    private String priceServiceRoutingKey;

    @Value("${queue-names.price-service}")
    private String priceServiceQueueName;


    @Bean
    public ProductService productService() {
        return new ProductService();
    }

    @Bean
    public PriceService priceService() {
        return new PriceService();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directXchangeName);
    }

    @Bean
    public Queue productServiceQueue() {
        return new Queue(productServiceQueueName);
    }

    @Bean
    public Binding productServiceBinding(DirectExchange directExchange, Queue productServiceQueue) {
        return BindingBuilder.bind(productServiceQueue).to(directExchange).with(productServiceRoutingKey);
    }

    @Bean
    public Queue priceServiceQueue() {
        return new Queue(priceServiceQueueName);
    }

    @Bean
    public Binding priceServiceBinding(DirectExchange directExchange, Queue priceServiceQueue) {
        return BindingBuilder.bind(priceServiceQueue).to(directExchange).with(priceServiceRoutingKey);
    }


}
