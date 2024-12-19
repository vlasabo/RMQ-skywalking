package com.example.consumer.repository;

import com.example.consumer.entity.ReceivedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vladimirsabo on 18.12.2024
 */
@Repository
public interface ReceivedMessageRepository extends JpaRepository<ReceivedMessage, Long> {
}
