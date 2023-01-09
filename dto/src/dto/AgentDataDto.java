package dto;

public class AgentDataDto {

    private String userName;
    private Integer numOfThreads;
    private Integer numOfTasks;

    public AgentDataDto(String userName, Integer numOfThreads, Integer numOfTasks) {
        this.userName = userName;
        this.numOfThreads = numOfThreads;
        this.numOfTasks = numOfTasks;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getNumOfThreads() {
        return numOfThreads;
    }

    public Integer getNumOfTasks() {
        return numOfTasks;
    }
}
