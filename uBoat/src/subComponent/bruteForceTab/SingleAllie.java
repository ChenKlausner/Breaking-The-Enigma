package subComponent.bruteForceTab;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SingleAllie {
    private SimpleStringProperty allieUserName;
    private SimpleIntegerProperty numOfAgents;
    private SimpleIntegerProperty taskSize;

    public SingleAllie(String allieUserName, Integer numOfAgents, Integer taskSize) {
        this.allieUserName = new SimpleStringProperty(allieUserName);
        this.numOfAgents = new SimpleIntegerProperty(numOfAgents);
        this.taskSize = new SimpleIntegerProperty(taskSize);
    }

    public String getAllieUserName() {
        return allieUserName.get();
    }

    public SimpleStringProperty allieUserNameProperty() {
        return allieUserName;
    }

    public int getNumOfAgents() {
        return numOfAgents.get();
    }

    public SimpleIntegerProperty numOfAgentsProperty() {
        return numOfAgents;
    }

    public int getTaskSize() {
        return taskSize.get();
    }

    public SimpleIntegerProperty taskSizeProperty() {
        return taskSize;
    }
}
