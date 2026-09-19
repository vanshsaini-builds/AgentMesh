package com.agentmesh.agentmesh.controller;

import com.agentmesh.agentmesh.pipeline.IncidentContext;
import com.agentmesh.agentmesh.service.Orchestrator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncidentController {

    private final Orchestrator orchestrator;

    public IncidentController(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/agent/execute")
    public IncidentContext executeAgent(@RequestBody String incidentDescription) {
        return orchestrator.process(incidentDescription);
    }
}