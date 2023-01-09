package subComponent.body;

import agentApp.AgentFullAppController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import subComponent.dashboard.DashboardTabController;

public class BodyController {
    private AgentFullAppController mainController;
    @FXML
    private BorderPane dashboardTabComponent;
    @FXML
    private DashboardTabController dashboardTabComponentController;


    @FXML
    public void initialize() {
        if (dashboardTabComponentController != null) {
            dashboardTabComponentController.setBodyController(this);
        }
    }

    public void setMainController(AgentFullAppController mainController) {
        this.mainController = mainController;
    }

    public void startToCheckIfNeedToWork() {
        dashboardTabComponentController.startCheckIfNeedToWork();
    }
    public SimpleStringProperty allieTeamNameProperty() {
        return mainController.allieTeamNameProperty();
    }
    public SimpleIntegerProperty numOfThreadsProperty() {
        return mainController.numOfThreadsProperty();
    }
    public SimpleStringProperty userNameProperty() {
        return mainController.userNameProperty();
    }

    public void bindBodyToFullApp() {
        dashboardTabComponentController.bindDashboardToApp();
    }

    public void startAgentContestData() {
        dashboardTabComponentController.startAgentContestData();
    }
}
