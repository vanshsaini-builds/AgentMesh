package com.agentmesh.agentmesh.service;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import org.springframework.stereotype.Service;

@Service
public class RootCauseAnalyzerAgent {

    private final GeminiClient geminiClient;

    public RootCauseAnalyzerAgent(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public void run(IncidentContext context) {
        String prompt = buildPrompt(context);
        String result = geminiClient.generateResponse(prompt);
        context.setRootCause(result.trim());
    }

    private String buildPrompt(IncidentContext context) {
        return """
            You are a Root-Cause-Analyzer agent in an incident-response system.
            Given the current incident's logs and a similar past incident,
            determine the most likely ROOT CAUSE.

            Current incident logs:
            %s

            Similar past incident (for reference):
            %s

            Respond in 1-3 sentences. State the root cause clearly and concretely
            (e.g. name the specific resource/config that is the problem). Do not
            suggest a fix here — only diagnose.
            """.formatted(context.getLogs(), context.getSimilarIncident());
    }
}