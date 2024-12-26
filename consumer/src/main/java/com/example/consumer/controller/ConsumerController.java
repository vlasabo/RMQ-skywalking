package com.example.consumer.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


/**
 * Created by vladimirsabo on 18.12.2024
 */
@RestController
@RequestMapping("/api/consumer")
@Slf4j
public class ConsumerController {

    @GetMapping("/check")
    public ResponseEntity<String> check(@RequestParam("checkParam") String checkParam,
    HttpServletRequest request) {
        log.info("check...");
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> log.info("Header: {} = {}", headerName, request.getHeader(headerName)));

        return ResponseEntity.ok("checked param: " + checkParam);
    }
}
