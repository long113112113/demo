package com.sparkmind.demo.service.impl;

import org.springframework.stereotype.Service;

import com.sparkmind.demo.service.CheckHealthService;

@Service
public class CheckHealthServiceImpl implements CheckHealthService {
    @Override
    public String checkHealth() {
        return "Health check passed!";
    }
    
}
