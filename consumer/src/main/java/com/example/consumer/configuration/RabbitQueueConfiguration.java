package com.example.consumer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vladimirsabo on 26.12.2024
 */
@Configuration
public class RabbitQueueConfiguration {

    @Bean
    public Queue myQueue() {
        return new Queue("my-queue", true); // durable = true
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange("my-exchange", true, false); // durable = true, auto-delete = false
    }

    @Bean
    public Binding binding(Queue myQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(myQueue)
                .to(myExchange)
                .with("my-routing-key");
    }
}
