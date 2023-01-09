package subComponent.body;

import dto.AgentResponse;
import dto.CodeConfiguration;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import subComponent.contest.ContestTabController;
import subComponent.dashboard.DashboardTabController;
import alliesApp.AlliesFullAppController;
import subComponent.dashboard.SingleContest;

public class BodyController {
    private AlliesFullAppController mainController;
    @FXML
    private BorderPane dashboardTabComponent;
    @FXML
    private DashboardTabController dashboardTabComponentController;
    @FXML
    private BorderPane contestTabComponent;
    @FXML
    private ContestTabController contestTabComponentController;

    @FXML
    public void initialize() {
        if (dashboardTabComponentController != null  && contestTabComponentController != null) {
            dashboardTabComponentController.setBodyController(this);
            contestTabComponentController.setBodyController(this);
        }
    }

    public void setMainController(AlliesFullAppController mainController) {
        this.mainController = mainController;
    }

    public void startDashboardRefresher() {
        dashboardTabComponentController.startContestDataRefresher();
        dashboardTabComponentController.startAgentTeamsDataRefresher();
    }

    public String getDescriptionOfCurrentSettings(CodeConfiguration codeConfiguration) {
        return mainController.getDescriptionOfCurrentSettings(codeConfiguration);
    }

    public void startUpdateContestData() {
        contestTabComponentController.startAgentContestData();
    }

    public void setUboatName(String uBoatUserName) {
        mainController.setUboatName(uBoatUserName);
    }

    public String getUboatName() {
        return mainController.getUboatName();
    }

    public void startContestRefresher() {
        contestTabComponentController.startAllieTeamsDataRefresher();
    }

    public void startTeamsAgentsProgress() {
        contestTabComponentController.startAgentProgressTableRefresher();
    }

    public void startTakingCandidates() {
        contestTabComponentController.startTakingCandidates();
    }

    public void startCheckWinner() {
        contestTabComponentController.startCheckIfThereIsWinner();
    }

    public void cleanDataInDashBoard() {
        dashboardTabComponentController.cleanData();
    }

    public void switchTabs() {
        mainController.switchTabs();
    }

    public void switchTabToContest() {
        mainController.switchTabToContest();
    }
}
