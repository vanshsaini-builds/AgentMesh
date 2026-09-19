package com.agentmesh.agentmesh.pipeline;

public class IncidentContext {

    private String incidentDescription;
    private String logs;
    private String similarIncident;
    private String rootCause;
    private String suggestedFix;
    private boolean verified;
    private String verificationReason;

    public IncidentContext(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    // Getters and setters for all fields

    public String getIncidentDescription() { return incidentDescription; }

    public String getLogs() { return logs; }
    public void setLogs(String logs) { this.logs = logs; }

    public String getSimilarIncident() { return similarIncident; }
    public void setSimilarIncident(String similarIncident) { this.similarIncident = similarIncident; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getSuggestedFix() { return suggestedFix; }
    public void setSuggestedFix(String suggestedFix) { this.suggestedFix = suggestedFix; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getVerificationReason() { return verificationReason; }
    public void setVerificationReason(String verificationReason) { this.verificationReason = verificationReason; }
}