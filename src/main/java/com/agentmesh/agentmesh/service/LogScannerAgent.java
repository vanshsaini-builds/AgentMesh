package com.agentmesh.agentmesh.service;

import org.springframework.stereotype.Service;

@Service
public class LogScannerAgent {

    private final GeminiClient geminiClient;
    private final LogTool logTool;

    public LogScannerAgent(GeminiClient geminiClient, LogTool logTool) {
        this.geminiClient = geminiClient;
        this.logTool = logTool;
    }

    private static final String SYSTEM_INSTRUCTIONS = """
        You are a Log-Scanner agent in an incident-response system. Your ONLY
        job is to fetch relevant logs for the affected service(s) using the
        available tool. You do NOT diagnose the problem or suggest fixes —
        other specialized agents will do that.

        Available tool:
        - query_logs(serviceName): returns recent log lines for a service.

        You MUST respond ONLY in this exact JSON format, nothing else:

        To call a tool:
        {"action": "call_tool", "tool": "query_logs", "serviceName": "<name>"}

        Once you believe you have fetched logs for all relevant service(s):
        {"action": "done"}

        Do not add any explanation outside the JSON. Only output valid JSON.
        """;

    public String fetchLogs(String incidentDescription) {
        StringBuilder conversation = new StringBuilder();
        conversation.append(SYSTEM_INSTRUCTIONS);
        conversation.append("\n\nIncident: ").append(incidentDescription);

        StringBuilder collectedLogs = new StringBuilder();
        int maxIterations = 5;

        for (int i = 0; i < maxIterations; i++) {

            String rawResponse = geminiClient.generateResponse(conversation.toString());
            String cleanJson = cleanJsonResponse(rawResponse);

            System.out.println("LogScanner iteration " + i + " | Gemini said: " + cleanJson);

            if (cleanJson.contains("\"action\": \"done\"") ||
                    cleanJson.contains("\"action\":\"done\"")) {
                return collectedLogs.toString();
            }

            if (cleanJson.contains("query_logs")) {
                String serviceName = extractServiceName(cleanJson);
                String logs = logTool.queryLogs(serviceName);

                // Raw tool-output seedha collect karte hain — LLM ke summary pe depend nahi karte
                collectedLogs.append("[").append(serviceName).append("]\n")
                        .append(logs).append("\n\n");

                conversation.append("\n\nTool result (query_logs for ")
                        .append(serviceName).append("):\n").append(logs);
            } else {
                conversation.append("\n\nYour response was not understood. Please respond in the correct JSON format.");
            }
        }

        return collectedLogs.toString(); // jitna mil chuka, wahi return kar do — silent-fail nahi
    }

    private String cleanJsonResponse(String raw) {
        return raw.replace("```json", "").replace("```", "").trim();
    }

    private String extractServiceName(String json) {
        try {
            int index = json.indexOf("serviceName");
            String sub = json.substring(index);
            String[] parts = sub.split(":");
            return parts[1].replaceAll("[\"{}]", "").trim();
        } catch (Exception e) {
            return "unknown-service";
        }
    }
}