package com.example.consumer.client;

import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by vladimirsabo on 20.12.2024
 */
@FeignClient(name = "producer-service", url = "${producer-service.url}")
public interface ProducerClient {

    @GetMapping("/api/producer/check")
    @Trace
    String check(@RequestParam("checkParam") String checkParam);
}
