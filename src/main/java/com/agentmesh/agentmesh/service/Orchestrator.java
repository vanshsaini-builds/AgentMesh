package com.agentmesh.agentmesh.service;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import org.springframework.stereotype.Service;

@Service
public class Orchestrator {

    private final LogScannerAgent logScannerAgent;
    private final PatternMatcherAgent patternMatcherAgent;
    private final RootCauseAnalyzerAgent rootCauseAnalyzerAgent;
    private final FixSuggesterAgent fixSuggesterAgent;
    private final VerifierAgent verifierAgent;

    public Orchestrator(LogScannerAgent logScannerAgent,
                        PatternMatcherAgent patternMatcherAgent,
                        RootCauseAnalyzerAgent rootCauseAnalyzerAgent,
                        FixSuggesterAgent fixSuggesterAgent,
                        VerifierAgent verifierAgent) {
        this.logScannerAgent = logScannerAgent;
        this.patternMatcherAgent = patternMatcherAgent;
        this.rootCauseAnalyzerAgent = rootCauseAnalyzerAgent;
        this.fixSuggesterAgent = fixSuggesterAgent;
        this.verifierAgent = verifierAgent;
    }

    public IncidentContext process(String incidentDescription) {
        IncidentContext context = new IncidentContext(incidentDescription);

        String logs = logScannerAgent.fetchLogs(incidentDescription);
        context.setLogs(logs);

        patternMatcherAgent.run(context);
        rootCauseAnalyzerAgent.run(context);
        fixSuggesterAgent.run(context);
        verifierAgent.run(context);

        return context;
    }
}