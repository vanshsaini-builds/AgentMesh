package com.agentmesh.agentmesh.service;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import org.springframework.stereotype.Service;

@Service
public class PatternMatcherAgent {

    private final GeminiClient geminiClient;

    public PatternMatcherAgent(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public void run(IncidentContext context) {
        String prompt = buildPrompt(context);
        String result = geminiClient.generateResponse(prompt);
        context.setSimilarIncident(result.trim());
    }

    private String buildPrompt(IncidentContext context) {
        return """
            You are a Pattern-Matcher agent in an incident-response system.
            Your job: given the current incident's logs, describe a plausible
            SIMILAR PAST INCIDENT (since we don't have a real vector memory yet,
            invent a realistic-sounding one based on the log pattern).

            Current incident logs:
            %s

            Respond in 2-3 sentences only. Describe the past incident and its root cause.
            """.formatted(context.getLogs());
    }
}