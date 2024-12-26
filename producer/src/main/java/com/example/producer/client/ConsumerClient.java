package com.example.producer.client;

import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by vladimirsabo on 20.12.2024
 */
@FeignClient(name = "consumer-service", url = "${consumer-service.url}")
public interface ConsumerClient {

    @GetMapping("/api/consumer/check")
    @Trace
    String check(@RequestParam("checkParam") String checkParam);
}
