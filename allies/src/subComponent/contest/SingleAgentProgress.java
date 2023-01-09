package subComponent.contest;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SingleAgentProgress {
    private SimpleStringProperty agentName;
    private SimpleIntegerProperty totalMissions;
    private SimpleIntegerProperty totalCompletedMissions;
    private SimpleIntegerProperty currentAmountOfMissionsInQueue;
    private SimpleIntegerProperty totalAmountOfCandidates;

    public SingleAgentProgress(String agentName, Integer totalMissions, Integer totalCompletedMissions, Integer currentAmountOfMissionsInQueue, Integer totalAmountOfCandidates) {
        this.agentName = new SimpleStringProperty(agentName);
        this.totalMissions = new SimpleIntegerProperty(totalMissions);
        this.totalCompletedMissions = new SimpleIntegerProperty(totalCompletedMissions);
        this.currentAmountOfMissionsInQueue = new SimpleIntegerProperty(currentAmountOfMissionsInQueue);
        this.totalAmountOfCandidates = new SimpleIntegerProperty(totalAmountOfCandidates);
    }

    public String getAgentName() {
        return agentName.get();
    }

    public SimpleStringProperty agentNameProperty() {
        return agentName;
    }

    public int getTotalMissions() {
        return totalMissions.get();
    }

    public SimpleIntegerProperty totalMissionsProperty() {
        return totalMissions;
    }

    public int getTotalCompletedMissions() {
        return totalCompletedMissions.get();
    }

    public SimpleIntegerProperty totalCompletedMissionsProperty() {
        return totalCompletedMissions;
    }

    public int getCurrentAmountOfMissionsInQueue() {
        return currentAmountOfMissionsInQueue.get();
    }

    public SimpleIntegerProperty currentAmountOfMissionsInQueueProperty() {
        return currentAmountOfMissionsInQueue;
    }

    public int getTotalAmountOfCandidates() {
        return totalAmountOfCandidates.get();
    }

    public SimpleIntegerProperty totalAmountOfCandidatesProperty() {
        return totalAmountOfCandidates;
    }
}
