package com.quantum.logs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantum.logs.annotations.ExcludeFromQuantumLog;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping({ "/log", "/log/" })
    public Map<String, String> testLogging() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a test message for Quantum AI Log");
        response.put("status", "success");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }

    @PostMapping({ "/testForPost", "/testForPost/" })
    public Map<String, String> testForPostLogging() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a test message for Quantum AI Log");
        response.put("status", "success");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }

    @GetMapping("/exclude")
    @ExcludeFromQuantumLog
    public Map<String, String> testExclusion() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a test message for Quantum AI Log");
        response.put("status", "success");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }
}
