package com.example.consumer.configureation;

import com.example.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * Created by vladimirsabo on 18.12.2024
 */
@Configuration
@RequiredArgsConstructor
public class ConsumerConfiguration {
    private final ConsumerService consumerService;
    @Bean
    public Consumer<Message<String>> receiveMessage() {
        return consumerService::processMessage;
    }
}
