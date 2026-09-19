package com.agentmesh.agentmesh.controller;

import com.agentmesh.agentmesh.service.GeminiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final GeminiClient geminiClient;

    // Spring automatically GeminiClient ka object yahan inject kar dega
    public TestController(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @GetMapping("/test")
    public String test(@RequestParam String prompt) {
        return geminiClient.generateResponse(prompt);
    }
}