package dto;

import java.util.List;

public class AllieResponse {
    private List<CandidateToDecrypt> candidates;
    private String agentName;

    public AllieResponse(List<CandidateToDecrypt> candidates, String agentName) {
        this.candidates = candidates;
        this.agentName = agentName;
    }

    public List<CandidateToDecrypt> getCandidates() {
        return candidates;
    }

    public String getAgentName() {
        return agentName;
    }
}
