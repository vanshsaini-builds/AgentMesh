package com.agentmesh.agentmesh.service;

import org.springframework.stereotype.Component;

@Component
public class LogTool {

    // Abhi ke liye hardcoded fake logs return kar rahe hain
    // Baad ke phases mein ye real log files/database se aayega
    public String queryLogs(String serviceName) {
        return """
            [2026-09-17 14:00:03] payment-service ERROR: Connection timeout after 30000ms
            [2026-09-17 14:00:05] payment-service ERROR: HikariPool-1 - Connection is not available, request timed out
            [2026-09-17 14:00:07] payment-service WARN: Active connections: 10/10 (pool exhausted)
            [2026-09-17 14:00:10] payment-service ERROR: HTTP 504 Gateway Timeout returned to client
            [2026-09-17 13:58:00] payment-service INFO: Incoming request rate: 52 req/sec
            """;
    }
}