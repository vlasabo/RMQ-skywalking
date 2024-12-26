package com.example.consumer.service;

import com.example.consumer.client.ProducerClient;
import com.example.consumer.entity.ReceivedMessage;
import com.example.consumer.repository.ReceivedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
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
    private final ProducerClient producerClient;

    @Trace
    public void processMessage(Message<String> message) {
        String payload = message.getPayload();
        log.info("Received message: [{}]", payload);
        Object sw8 = message.getHeaders().getOrDefault("sw8", "");
        var sw8String = (String) sw8;
        log.info("sw8Header = [{}]", sw8String);
        ActiveSpan.tag("traceId", TraceContext.traceId());
        log.info("TraceContext.traceId() = [{}]", TraceContext.traceId());
        ReceivedMessage receivedMessage = new ReceivedMessage();
        receivedMessage.setContent(payload);

        receivedMessageRepository.save(receivedMessage);

        log.info("Message saved to database: [{}]", receivedMessage);
//        log.info("TraceContext.traceId() = [{}]", TraceContext.traceId());


        var result = producerClient.check("check param ".concat(payload));
        ReceivedMessage checkedMessage = ReceivedMessage.builder()
                .content(result)
                .build();
        receivedMessageRepository.save(checkedMessage);
        log.info("checkedMessage saved to database: [{}]", checkedMessage);
    }
}
