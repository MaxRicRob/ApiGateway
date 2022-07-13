package com.example.ApiGateway.configuration;


import com.example.ApiGateway.domain.CurrencyService;
import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.ProductService;
import com.example.ApiGateway.error.ErrorResponseException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.util.ErrorHandler;

@Configuration
public class RabbitConfiguration {

    @Value("${xchange.name}")
    private String directXchangeName;
    @Value("${routing-keys.product-service}")
    private String productServiceRoutingKey;
    @Value("${routing-keys.price-service}")
    private String priceServiceRoutingKey;
    @Value("${routing-keys.currency-service}")
    private String currencyServiceRoutingKey;
    @Value("${queue-names.product-service}")
    private String productServiceQueueName;
    @Value("${queue-names.price-service}")
    private String priceServiceQueueName;
    @Value("${queue-names.currency-service}")
    private String currencyServiceQueueName;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directXchangeName);
    }

    @Bean
    public ProductService productService() {
        return new ProductService();
    }

    @Bean
    public PriceService priceService() {
        return new PriceService();
    }

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService();
    }

    @Bean
    public Queue productServiceQueue() {
        return new Queue(productServiceQueueName);
    }

    @Bean
    public Queue priceServiceQueue() {
        return new Queue(priceServiceQueueName);
    }

    @Bean
    public Queue currencyServiceQueue() {
        return new Queue(currencyServiceQueueName);
    }

    @Bean
    public Binding productServiceBinding(DirectExchange directExchange, Queue productServiceQueue) {
        return BindingBuilder.bind(productServiceQueue).to(directExchange).with(productServiceRoutingKey);
    }

    @Bean
    public Binding priceServiceBinding(DirectExchange directExchange, Queue priceServiceQueue) {
        return BindingBuilder.bind(priceServiceQueue).to(directExchange).with(priceServiceRoutingKey);
    }

    @Bean
    public Binding currencyServiceBinding(DirectExchange directExchange, Queue currencyServiceQueue) {
        return BindingBuilder.bind(currencyServiceQueue).to(directExchange).with(currencyServiceRoutingKey);
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new MyFatalExceptionStrategy();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    private static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

        @Override
        public boolean isFatal(Throwable t) {
            return !(t.getCause() instanceof ErrorResponseException);
        }
    }

}
