package users;

import dto.AgentProgressDto;

public class Agent {
    private String userName;
    private String alliesTeamName;
    private Integer numOfThreads;
    private Integer numOfTasks;
    private AgentProgressDto agentProgress;

    public Agent(String userName, String alliesTeamName, Integer numOfThreads, Integer numOfTasks) {
        this.userName = userName;
        this.alliesTeamName = alliesTeamName;
        this.numOfThreads = numOfThreads;
        this.numOfTasks = numOfTasks;
        this.agentProgress = null;
    }

    public String getUserName() {
        return userName;
    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public Integer getNumOfThreads() {
        return numOfThreads;
    }

    public Integer getNumOfTasks() {
        return numOfTasks;
    }

    public AgentProgressDto getAgentProgress() {
        return agentProgress;
    }

    public void setAgentProgress(AgentProgressDto agentProgress) {
        this.agentProgress = agentProgress;
    }
}
