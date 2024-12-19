package com.example.consumer.service;

import com.example.consumer.entity.ReceivedMessage;
import com.example.consumer.repository.ReceivedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


/**
 * Created by vladimirsabo on 18.12.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final ReceivedMessageRepository receivedMessageRepository;

    public void processMessage(Message<String> message) {
        String payload = message.getPayload();
        log.info("Received message: [{}]", payload);
        Object sw8 = message.getHeaders().getOrDefault("sw8", "");
        var sw8String = (String) sw8;
        log.info("sw8Header = [{}]", sw8String);
        ReceivedMessage receivedMessage = new ReceivedMessage();
        receivedMessage.setContent(payload);
        receivedMessageRepository.save(receivedMessage);

        log.info("Message saved to database: [{}]", payload);
    }
}
