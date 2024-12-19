package com.example.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;


/**
 * Created by vladimirsabo on 18.12.2024
 */
@RestController
@RequestMapping("/api/producer")
@Slf4j
public class ProducerController {
    private final StreamBridge streamBridge;

    public ProducerController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        log.info("try to send message [{}]", message);
        Message<String> queueMessage = MessageBuilder.withPayload(message).build();
        log.info("all headers before send = [{}]", queueMessage.getHeaders());
        streamBridge.send("sendMessage-out-0", queueMessage);
        var sw8Header = queueMessage.getHeaders().getOrDefault("sw8", "sw8 header isn't present");
        log.info("sw8Header = [{}]", sw8Header);
        log.info("all headers after send = [{}]", queueMessage.getHeaders());
        return ResponseEntity.ok("Message sent: " + message);
    }
}
