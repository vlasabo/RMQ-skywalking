package com.example.producer.controller;

import com.example.producer.client.ConsumerClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections;


/**
 * Created by vladimirsabo on 18.12.2024
 */
@RestController
@RequestMapping("/api/producer")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {
    private final StreamBridge streamBridge;
    private final ConsumerClient consumerClient;


    @PostMapping("/send")
    @Trace
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        log.info("try to send message [{}]", message);
        log.info("TraceContext.traceId() = [{}]", TraceContext.traceId());

        // Формируем sw8-заголовок вручную
//        var sw8Header = String.format(
//                "1-%s-%s-%d-%s-%s-%s-%s",
//                base64Encode(TraceContext.traceId()),                // traceId
//                base64Encode(TraceContext.segmentId()),              // segmentId
//                TraceContext.spanId(),                               // spanId
//                base64Encode("producer-service"),              // parentService
//                base64Encode("producer-instance"),             // parentServiceInstance
//                base64Encode("sendMessage"),                   // endpoint
//                base64Encode("rabbitmq://my-queue")            // networkAddress
//        );
//        TraceContext.putCorrelation("sw8", sw8Header);
        Message<String> queueMessage = MessageBuilder
                .withPayload(message)
//                .setHeader("sw8", sw8Header)
                .build();
        log.info("all headers before send = [{}]", queueMessage.getHeaders());
        streamBridge.send("sendMessage-out-0", queueMessage);
        log.info("all headers after send = [{}]", queueMessage.getHeaders());
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/check")
    @Trace
    public ResponseEntity<String> check(@RequestParam("checkParam") String checkParam,
                                        HttpServletRequest request) {
        log.info("check...");
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> log.info("Header: {} = {}", headerName, request.getHeader(headerName)));
        consumerClient.check(checkParam);
        return ResponseEntity.ok("checked param: " + checkParam);
    }

    private String base64Encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}
