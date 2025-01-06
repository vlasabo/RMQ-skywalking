package com.example.consumer.service;

import com.example.consumer.client.ProducerClient;
import com.example.consumer.entity.ReceivedMessage;
import com.example.consumer.repository.ReceivedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

import com.rabbitmq.client.Channel;

/**
 * Created by vladimirsabo on 18.12.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final ReceivedMessageRepository receivedMessageRepository;
    private final ProducerClient producerClient;

    @RabbitListener(queues = "my-queue", ackMode = "MANUAL")
    @Trace
    public void processMessage(org.springframework.amqp.core.Message message, Channel channel) {
        try {
            // Логируем sw8 заголовок
            String sw8Header = message.getMessageProperties().getHeader("sw8");
            log.info("Received sw8Header: [{}]", sw8Header);

            // Обработка сообщения
            String payload = new String(message.getBody());
            log.info("Received message payload: [{}]", payload);
            ActiveSpan.tag("traceId", TraceContext.traceId());
            log.info("TraceContext.traceId() = [{}]", TraceContext.traceId());

            ReceivedMessage receivedMessage = new ReceivedMessage();
            receivedMessage.setContent(payload);
            receivedMessageRepository.save(receivedMessage);
            log.info("Message saved to database: [{}]", receivedMessage);

            var result = producerClient.check("check param ".concat(payload));
            ReceivedMessage checkedMessage = ReceivedMessage.builder()
                    .content(result)
                    .build();
            receivedMessageRepository.save(checkedMessage); //отправка вызова во второй микр
            log.info("Checked message saved to database: [{}]", checkedMessage);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error processing message", e);
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                log.error("Failed to nack message", ioException);
            }
        }
    }
}