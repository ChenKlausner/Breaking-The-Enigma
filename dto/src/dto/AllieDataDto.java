package dto;

public class AllieDataDto {
    private String userName;
    private Integer amountOfAgents;
    private Integer taskSize;

    public AllieDataDto(String userName, Integer amountOfAgents, Integer taskSize) {
        this.userName = userName;
        this.amountOfAgents = amountOfAgents;
        this.taskSize = taskSize;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getAmountOfAgents() {
        return amountOfAgents;
    }

    public Integer getTaskSize() {
        return taskSize;
    }
}
