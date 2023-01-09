package dto;

import dto.CandidateToDecrypt;

import java.util.List;

public class AgentResponse {
    private List<CandidateToDecrypt> candidates;

    private boolean isLastResponse;

    public AgentResponse(List<CandidateToDecrypt> candidates, boolean isLastResponse) {
        this.candidates = candidates;
        this.isLastResponse = isLastResponse;
    }

    public List<CandidateToDecrypt> getCandidates() {
        return candidates;
    }

    public boolean isLastResponse() {
        return isLastResponse;
    }
}
