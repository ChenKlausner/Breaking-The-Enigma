package dto;

public class AgentProgressDto {
    private String agentName;
    private Integer totalMissions;
    private Integer totalCompletedMissions;
    private Integer currentAmountOfMissionsInQueue;
    private Integer totalAmountOfCandidates;

    public AgentProgressDto(String agentName, Integer totalMissions, Integer totalCompletedMissions, Integer currentAmountOfMissionsInQueue, Integer totalAmountOfCandidates) {
        this.agentName = agentName;
        this.totalMissions = totalMissions;
        this.totalCompletedMissions = totalCompletedMissions;
        this.currentAmountOfMissionsInQueue = currentAmountOfMissionsInQueue;
        this.totalAmountOfCandidates = totalAmountOfCandidates;
    }

    public String getAgentName() {
        return agentName;
    }

    public Integer getTotalMissions() {
        return totalMissions;
    }

    public Integer getTotalCompletedMissions() {
        return totalCompletedMissions;
    }

    public Integer getCurrentAmountOfMissionsInQueue() {
        return currentAmountOfMissionsInQueue;
    }

    public Integer getTotalAmountOfCandidates() {
        return totalAmountOfCandidates;
    }
}
