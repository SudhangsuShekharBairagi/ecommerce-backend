package com.shekhar.ecom_proj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class TestController {

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("UP");
    }
}