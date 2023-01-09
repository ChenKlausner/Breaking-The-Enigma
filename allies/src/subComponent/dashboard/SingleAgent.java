package subComponent.dashboard;

import javafx.beans.property.SimpleStringProperty;

public class SingleAgent {
    private SimpleStringProperty agentUserName;
    private SimpleStringProperty amountOfThreads;
    private SimpleStringProperty amountOfTasks;

    public SingleAgent(String agentUserName, Integer amountOfThreads, Integer amountOfTasks) {
        this.agentUserName = new SimpleStringProperty(agentUserName);
        this.amountOfThreads = new SimpleStringProperty(amountOfThreads.toString());
        this.amountOfTasks = new SimpleStringProperty(amountOfTasks.toString());
    }

    public String getAgentUserName() {
        return agentUserName.get();
    }

    public SimpleStringProperty agentUserNameProperty() {
        return agentUserName;
    }

    public String getAmountOfThreads() {
        return amountOfThreads.get();
    }

    public SimpleStringProperty amountOfThreadsProperty() {
        return amountOfThreads;
    }

    public String getAmountOfTasks() {
        return amountOfTasks.get();
    }

    public SimpleStringProperty amountOfTasksProperty() {
        return amountOfTasks;
    }
}
