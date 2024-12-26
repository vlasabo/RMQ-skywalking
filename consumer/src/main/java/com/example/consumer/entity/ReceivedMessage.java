package com.example.consumer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.time.LocalDateTime;

/**
 * Created by vladimirsabo on 18.12.2024
 */

@Entity
@Table(name = "received_message")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 1024)
    private String sw8Field;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.sw8Field = TraceContext.traceId();
    }
}
