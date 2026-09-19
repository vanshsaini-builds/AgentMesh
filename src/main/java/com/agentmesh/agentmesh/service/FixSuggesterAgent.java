package com.agentmesh.agentmesh.service;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import org.springframework.stereotype.Service;

@Service
public class FixSuggesterAgent {

    private final GeminiClient geminiClient;

    public FixSuggesterAgent(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public void run(IncidentContext context) {
        String prompt = buildPrompt(context);
        String result = geminiClient.generateResponse(prompt);
        context.setSuggestedFix(result.trim());
    }

    private String buildPrompt(IncidentContext context) {
        return """
            You are a Fix-Suggester agent in an incident-response system.
            Given the diagnosed root cause, suggest ONE concrete, actionable fix.

            Root cause:
            %s

            Respond in 1-2 sentences. Be specific (e.g. exact config change,
            exact command, exact value to change). Do not restate the root cause.
            """.formatted(context.getRootCause());
    }
}