package subComponent.dashboard;

import agent.AgentManager;
import agent.AgentTask;
import dto.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import subComponent.body.BodyController;
import util.Constants;

import java.io.IOException;
import java.util.*;

import static util.Constants.REFRESH_RATE;
import static util.http.HttpClientUtil.HTTP_CLIENT;


public class DashboardTabController {
    private AgentManager agentManager;
    private BodyController bodyController;
    @FXML
    private TableView<SingleCandidate> candidatesTableView;
    @FXML
    private TableColumn<SingleCandidate, String> candidateCol;
    @FXML
    private TableColumn<SingleCandidate, String> codeConfigurationCol;
    @FXML
    private Label allieTeamLabel;
    @FXML
    private Label battlefieldNameLabel;
    @FXML
    private Label numOfAliieTeamsLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label totalMissionLabel;
    @FXML
    private Label totalCompletedMissionsLabel;
    @FXML
    private Label amountOfMissionInQueueLabel;
    @FXML
    private Label totalCandidatesLabel;
    private SimpleStringProperty userName;
    private SimpleIntegerProperty numOfThreads;
    private Timer startWorkingTimer;
    private TimerTask checkIfNeedToWorkRefresher;
    private Timer agentContestDataTimer;
    private TimerTask agentContestDataRefresher;
    private Timer getAgentTaskTimer;
    private TimerTask agentTaskListRefresher;
    private Timer agentProgressTimer;
    private TimerTask agentProgressRefresher;
    private Timer agentResponseTimer;
    private TimerTask agentResponseRefresher;
    private Timer checkWinnerTimer;
    private TimerTask checkWinnerRefresher;
    private Timer cleanDataTimer;
    private TimerTask checkCleanDataRefresher;


    @FXML
    public void initialize() {
        candidateCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("decryptMsg"));
        codeConfigurationCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("codeConfiguration"));
        numOfThreads = new SimpleIntegerProperty();
        userName = new SimpleStringProperty();
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    public void bindDashboardToApp() {
        allieTeamLabel.textProperty().bind(bodyController.allieTeamNameProperty());
        numOfThreads.bind(bodyController.numOfThreadsProperty());
        userName.bind(bodyController.userNameProperty());
    }

    public void agentContestDataConsumer(BattlefieldDto battlefieldDto) {
        Platform.runLater(() -> {
            if (battlefieldDto != null) {
                battlefieldNameLabel.setText(battlefieldDto.getBattleName());
                numOfAliieTeamsLabel.setText(battlefieldDto.getListedTeamsVsNeededTeams());
                difficultyLabel.setText(battlefieldDto.getDifficultyLevel());
                statusLabel.setText(battlefieldDto.getContestStatus());
            }
        });
    }

    public void startAgentContestData() {
        agentContestDataRefresher = new ContestDataRefresher(this::agentContestDataConsumer);
        agentContestDataTimer = new Timer();
        agentContestDataTimer.schedule(agentContestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void isReadyConsumer(Boolean isReady) {
        if (isReady) {
            startToWorkOnAgentTask();
            startWorkingTimer.cancel();
        }
    }

    public void startCheckIfNeedToWork() {
        checkIfNeedToWorkRefresher = new CheckIfNeedToWorkRefresher(this::isReadyConsumer);
        startWorkingTimer = new Timer();
        startWorkingTimer.schedule(checkIfNeedToWorkRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void startToWorkOnAgentTask() {
        agentManager = new AgentManager(numOfThreads.getValue());
        agentManager.startExecuteTasks();
        startGetAgentTaskList();
        startAgentProgress();
        startAgentResponse();
        startCheckIfThereIsWinner();
    }

    private void agentListTaskConsumer(List<AgentTask> agentTaskList) {
        for (AgentTask agentTask : agentTaskList) {
            agentTask.setTotalCandidates(agentManager.getTotalCandidates());
            agentTask.setTotalCompletedMissions(agentManager.getTotalCompletedMissions());
            agentTask.setAgentResponse(agentManager.getAgentResponse());
            agentManager.addAgentTask(agentTask);
            agentManager.addOneToTotalMissionsCounter();
        }
        updateTotalMission(agentManager.getTotalMissions());
        updateCurrentAmountOfMissionInQueue(agentManager.getCurrentAmountInQueue());
    }

    private void startGetAgentTaskList() {
        agentTaskListRefresher = new AgentTaskListRefresher(this::agentListTaskConsumer, agentManager.isEmptyAgentTaskBlockingDeque());
        getAgentTaskTimer = new Timer();
        getAgentTaskTimer.schedule(agentTaskListRefresher, 500, 500);
    }

    public void updateTotalMission(int totalMission) {
        Platform.runLater(() -> {
            totalMissionLabel.setText(String.valueOf(totalMission));
        });
    }

    public void updateCurrentAmountOfMissionInQueue(int currentAmountOfAgentTask){
        Platform.runLater(() -> {
            amountOfMissionInQueueLabel.setText(String.valueOf(currentAmountOfAgentTask));
        });
    }


    private void updateAgentProgress(AgentProgressDto agentProgressDto) {
        Platform.runLater(() -> {
            totalMissionLabel.setText(agentProgressDto.getTotalMissions().toString());
            totalCompletedMissionsLabel.setText(agentProgressDto.getTotalCompletedMissions().toString());
            amountOfMissionInQueueLabel.setText(agentProgressDto.getCurrentAmountOfMissionsInQueue().toString());
            totalCandidatesLabel.setText(agentProgressDto.getTotalAmountOfCandidates().toString());
        });
    }

    private void startAgentProgress() {
        agentProgressRefresher = new AgentProgressRefresher(this::updateAgentProgress, getAgentManager(), userName.getValue());
        agentProgressTimer = new Timer();
        agentProgressTimer.schedule(agentProgressRefresher, 2000, 2000);
    }

    private void updateCandidatesTableView(AgentResponse agentResponse) {
        Platform.runLater(() -> {
            for (int i = 0; i < agentResponse.getCandidates().size(); i++) {
                String candidate = agentResponse.getCandidates().get(i).getDecryptMsg();
                String codeConfiguration = getDescriptionOfCurrentSettings(agentResponse.getCandidates().get(i).getCodeConfiguration());
                candidatesTableView.getItems().add(new SingleCandidate(candidate, codeConfiguration));
            }
        });
    }

    private void startAgentResponse() {
        agentResponseRefresher = new AgentResponseRefresher(this::updateCandidatesTableView, agentManager.getAgentResponse());
        agentResponseTimer = new Timer();
        agentResponseTimer.schedule(agentResponseRefresher, 500, 500);
    }

    public void doWhenContestIsDone(boolean done) {
        if (done) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            agentResponseTimer.cancel();
            agentProgressTimer.cancel();
            getAgentTaskTimer.cancel();
            checkWinnerTimer.cancel();
            //new AgentProgressRefresher(this::updateAgentProgress, getAgentManager(), userName.getValue()).run();
            agentManager.stopExecuteTasks();
            agentManager.clearAgentTask();
            agentManager.clearAgentResponse();
            agentManager.clearData();
            startCheckNeedToCleanData();
        }
    }

    public void startCheckIfThereIsWinner() {
        checkWinnerRefresher = new CheckWinnerRefresher(this::doWhenContestIsDone);
        checkWinnerTimer = new Timer();
        checkWinnerTimer.schedule(checkWinnerRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void startCheckNeedToCleanData() {
        checkCleanDataRefresher = new CheckCleanDataRefresher(this::cleanAgentData);
        cleanDataTimer = new Timer();
        cleanDataTimer.schedule(checkCleanDataRefresher, 1000, 1000);
    }

    private void cleanAgentData(boolean cleanData) {
        if (cleanData) {
            Platform.runLater(() -> {
                battlefieldNameLabel.setText("");
                numOfAliieTeamsLabel.setText("");
                difficultyLabel.setText("");
                statusLabel.setText("");
                totalMissionLabel.setText("");
                totalCompletedMissionsLabel.setText("");
                amountOfMissionInQueueLabel.setText("");
                totalCandidatesLabel.setText("");
                candidatesTableView.getItems().clear();
            });
            startCheckIfNeedToWork();
            cleanDataTimer.cancel();
        }
    }

    public AgentManager getAgentManager() {
        return agentManager;
    }

    public String getDescriptionOfCurrentSettings(CodeConfiguration codeConfiguration) {
        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        StringBuilder str3 = new StringBuilder();
        Set<Integer> set = codeConfiguration.getRotorsInUse().keySet();
        Iterator<Integer> itr = set.iterator();
        List<Integer> alKeys = new ArrayList<Integer>(codeConfiguration.getRotorsInUse().keySet());
        Collections.reverse(alKeys);
        str1.append("<");
        str2.append("<");
        for (Integer strKey : alKeys) {
            if (alKeys.get(0) == strKey) {
                str1.append(strKey);
            } else {
                str1.append(",").append(strKey);
                str2.append(",");
            }
            str2.append(codeConfiguration.getRotorsInUse().get(strKey));
            str2.append("(" + codeConfiguration.getNotchDistanceFromWindow().get(strKey) + ")");
        }
        str1.append(">");
        str2.append(">");
        str1.append(str2).append("<" + codeConfiguration.getReflectorId() + ">");
        if (codeConfiguration.getPlugsInUse().size() != 0) {
            str3.append("<");
            codeConfiguration.getPlugsInUse().forEach((k, v) -> str3.append(k).append("|").append(v).append(","));
            str3.deleteCharAt(str3.length() - 1);
            str3.append(">");
        }
        str1.append(str3);
        return str1.toString();
    }
}
