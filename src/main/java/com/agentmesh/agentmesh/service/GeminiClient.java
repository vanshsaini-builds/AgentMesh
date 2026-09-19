package com.agentmesh.agentmesh.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class GeminiClient {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final String MODEL_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponse(String prompt) {
        int maxRetries = 3;
        long delayMillis = 2000; // 2 seconds

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return callGemini(prompt);
            } catch (HttpServerErrorException.ServiceUnavailable e) {
                System.out.println("Gemini 503 (attempt " + attempt + "/" + maxRetries + ") — retrying...");
                if (attempt == maxRetries) {
                    throw e; // teesri baar bhi fail, ab genuinely fail maano
                }
                try {
                    Thread.sleep(delayMillis * attempt); // 2s, phir 4s
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IllegalStateException("Unreachable"); // compiler ko khush karne ke liye
    }

    private String callGemini(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(MODEL_URL, requestEntity, Map.class);

        Map responseBody = response.getBody();
        List candidates = (List) responseBody.get("candidates");
        Map firstCandidate = (Map) candidates.get(0);
        Map content = (Map) firstCandidate.get("content");
        List parts = (List) content.get("parts");
        Map firstPart = (Map) parts.get(0);

        return (String) firstPart.get("text");
    }
}