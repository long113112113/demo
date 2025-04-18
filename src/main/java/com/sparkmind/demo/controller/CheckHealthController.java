package com.sparkmind.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sparkmind.demo.service.CheckHealthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckHealthController {
    private final CheckHealthService checkHealthService;
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok(this.checkHealthService.checkHealth());
    }
}

