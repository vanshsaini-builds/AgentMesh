package com.agentmesh.agentmesh.service;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import org.springframework.stereotype.Service;

@Service
public class VerifierAgent {

    private final GeminiClient geminiClient;

    public VerifierAgent(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public void run(IncidentContext context) {
        String prompt = buildPrompt(context);
        String rawResponse = geminiClient.generateResponse(prompt);
        String cleanJson = rawResponse.replace("```json", "").replace("```", "").trim();

        context.setVerified(extractVerified(cleanJson));
        context.setVerificationReason(extractReason(cleanJson));
    }

    private String buildPrompt(IncidentContext context) {
        return """
            You are a Verifier (Critic) agent in an incident-response system.
            Check whether the suggested fix genuinely addresses the root cause,
            and whether it could introduce new problems (e.g. resource limits,
            side effects on other services).

            Root cause:
            %s

            Suggested fix:
            %s

            Respond ONLY in this exact JSON format, nothing else:
            {"verified": true, "reason": "<short reason>"}
            or
            {"verified": false, "reason": "<short reason>"}
            """.formatted(context.getRootCause(), context.getSuggestedFix());
    }

    private boolean extractVerified(String json) {
        return json.contains("\"verified\": true") || json.contains("\"verified\":true");
    }

    private String extractReason(String json) {
        try {
            int index = json.indexOf("\"reason\"");
            String sub = json.substring(index);
            int firstQuote = sub.indexOf(":") + 1;
            String reasonPart = sub.substring(firstQuote).trim();
            // reasonPart kuch aisa dikhega: "some reason"}
            return reasonPart.substring(1, reasonPart.lastIndexOf("\""));
        } catch (Exception e) {
            return "Could not parse verification reason.";
        }
    }
}