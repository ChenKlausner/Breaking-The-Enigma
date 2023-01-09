package subComponent.contest;

import dto.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import subComponent.body.BodyController;
import util.Constants;

import java.io.IOException;
import java.util.*;

import static util.Constants.REFRESH_RATE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class ContestTabController {
    private BodyController bodyController;
    @FXML
    private Label battlefieldNameLabel;
    @FXML
    private Label numOfAliieTeamsLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label secretMsgLabel;
    @FXML
    private TableView<SingleAllie> alliesTeamDataTableView;
    @FXML
    private TableColumn<SingleAllie, String> teamNameCol;
    @FXML
    private TableColumn<SingleAllie, Integer> numOfAgentCol;
    @FXML
    private TableColumn<SingleAllie, Integer> taskSizeCol;

    @FXML
    private TableView<SingleAgentProgress> teamAgentProgressDataTableView;

    @FXML
    private TableColumn<SingleAgentProgress, String> agentNameCol;

    @FXML
    private TableColumn<SingleAgentProgress, Integer> totalMissionsCol;

    @FXML
    private TableColumn<SingleAgentProgress, Integer> missionsInQueueCol;

    @FXML
    private TableColumn<SingleAgentProgress, Integer> totalCandidatesCol;
    @FXML
    private TableView<SingleCandidate> candidatesTableView;
    @FXML
    private TableColumn<SingleCandidate, String> candidateCol;
    @FXML
    private TableColumn<SingleCandidate, String> candidateAgentNameCol;
    @FXML
    private TableColumn<SingleCandidate, String> codeConfigurationCol;

    @FXML
    private AnchorPane winnerAncorPane;
    @FXML
    private Label winnerNameLabel;

    private Timer allieTeamTimer;
    private TimerTask allieTeamDataRefresher;
    private Timer agentProgressTableTimer;
    private TimerTask agentProgressTableRefresher;
    private Timer candidatesResponseTimer;
    private TimerTask takingResponseRefresher;
    private Timer agentContestDataTimer;
    private TimerTask agentContestDataRefresher;
    private Timer checkWinnerTimer;
    private TimerTask checkWinnerRefresher;
    private Timer logoutTimer;
    private TimerTask checkLogoutRefresher;

    @FXML
    public void initialize() {
        teamNameCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, String>("allieUserName"));
        numOfAgentCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, Integer>("numOfAgents"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, Integer>("taskSize"));

        agentNameCol.setCellValueFactory(new PropertyValueFactory<SingleAgentProgress, String>("agentName"));
        totalMissionsCol.setCellValueFactory(new PropertyValueFactory<SingleAgentProgress, Integer>("totalMissions"));
        missionsInQueueCol.setCellValueFactory(new PropertyValueFactory<SingleAgentProgress, Integer>("currentAmountOfMissionsInQueue"));
        totalCandidatesCol.setCellValueFactory(new PropertyValueFactory<SingleAgentProgress, Integer>("totalAmountOfCandidates"));

        candidateAgentNameCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("candidateAgentName"));
        candidateCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("decryptMsg"));
        codeConfigurationCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("codeConfiguration"));
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }


    private void updateAllieTeamsTableView(List<AllieDataDto> allieDataDtoList) {
        Platform.runLater(() -> {
            ObservableList<SingleAllie> allieTeamsData = FXCollections.observableArrayList();
            for (AllieDataDto allieDataDto : allieDataDtoList) {
                allieTeamsData.add(new SingleAllie(allieDataDto.getUserName(), allieDataDto.getAmountOfAgents(), allieDataDto.getTaskSize()));
            }
            alliesTeamDataTableView.getItems().clear();
            alliesTeamDataTableView.setItems(allieTeamsData);
        });
    }

    public void startAllieTeamsDataRefresher() {
        allieTeamDataRefresher = new AlliesTeamDataRefresher(this::updateAllieTeamsTableView, bodyController.getUboatName());
        allieTeamTimer = new Timer();
        allieTeamTimer.schedule(allieTeamDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void updateAgentProgressTableView(List<AgentProgressDto> agentProgressDtoList) {
        if (agentProgressDtoList != null) {
            Platform.runLater(() -> {
                ObservableList<SingleAgentProgress> singleAgentProgressObservableList = FXCollections.observableArrayList();
                for (AgentProgressDto agentProgressDto : agentProgressDtoList) {
                    singleAgentProgressObservableList.add(new SingleAgentProgress(agentProgressDto.getAgentName(), agentProgressDto.getTotalMissions(), agentProgressDto.getTotalCompletedMissions(), agentProgressDto.getCurrentAmountOfMissionsInQueue(), agentProgressDto.getTotalAmountOfCandidates()));
                }
                teamAgentProgressDataTableView.getItems().clear();
                teamAgentProgressDataTableView.setItems(singleAgentProgressObservableList);
            });
        }
    }

    public void startAgentProgressTableRefresher() {
        agentProgressTableRefresher = new AgentProgressRefresher(this::updateAgentProgressTableView);
        agentProgressTableTimer = new Timer();
        agentProgressTableTimer.schedule(agentProgressTableRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void allieResponseConsumer(AllieResponse allieResponse) {
        if (allieResponse != null) {
            Platform.runLater(() -> {
                for (int i = 0; i < allieResponse.getCandidates().size(); i++) {
                    String agentName = allieResponse.getAgentName();
                    String candidate = allieResponse.getCandidates().get(i).getDecryptMsg();
                    String codeConfiguration = getDescriptionOfCurrentSettings(allieResponse.getCandidates().get(i).getCodeConfiguration());
                    candidatesTableView.getItems().add(new SingleCandidate(agentName, candidate, codeConfiguration));
                }
            });
        }
    }

    public void startTakingCandidates() {
        takingResponseRefresher = new TakingResponseRefresher(this::allieResponseConsumer, bodyController.getUboatName());
        candidatesResponseTimer = new Timer();
        candidatesResponseTimer.schedule(takingResponseRefresher, 500, 500);
    }

    public void agentContestDataConsumer(BattlefieldDto battlefieldDto) {
        Platform.runLater(() -> {
            if (battlefieldDto != null) {
                battlefieldNameLabel.setText(battlefieldDto.getBattleName());
                numOfAliieTeamsLabel.setText(battlefieldDto.getListedTeamsVsNeededTeams());
                difficultyLabel.setText(battlefieldDto.getDifficultyLevel());
                statusLabel.setText(battlefieldDto.getContestStatus());
                if (battlefieldDto.getContestStatus().equals("ACTIVE")) {
                    //agentContestDataTimer.cancel();
                    updateSecretMsg();
                }
            }
        });

    }

    public void startAgentContestData() {
        agentContestDataRefresher = new SignContestDataRefresher(this::agentContestDataConsumer);
        agentContestDataTimer = new Timer();
        agentContestDataTimer.schedule(agentContestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateSecretMsg() {
        String finalUrl = HttpUrl
                .parse(Constants.SECRET_MSG)
                .newBuilder()
                .addQueryParameter("uboatName", bodyController.getUboatName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            String secretMsg = response.body().string();
            Platform.runLater(() -> {
                secretMsgLabel.setText(secretMsg);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void winnerResponseConsumer(WinnerResponse winnerResponse) {
        if (winnerResponse.getWinnerWasFound()) {
            Platform.runLater(() -> {
                bodyController.switchTabToContest();
                winnerAncorPane.setVisible(true);
                winnerNameLabel.setText(winnerResponse.getWinnerName());
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkWinnerTimer.cancel();
            candidatesResponseTimer.cancel();
            agentProgressTableTimer.cancel();
            allieTeamTimer.cancel();
            startCheckIfUBoatLogout();
        }
    }

    public void startCheckIfThereIsWinner() {
        checkWinnerRefresher = new CheckWinnerRefresher(this::winnerResponseConsumer, bodyController.getUboatName());
        checkWinnerTimer = new Timer();
        checkWinnerTimer.schedule(checkWinnerRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public synchronized void logoutConsumer(Boolean logoutResponse) {
        if (logoutResponse){
            logoutTimer.cancel();
            Platform.runLater(() -> {
                winnerNameLabel.setText("");
                winnerAncorPane.setVisible(false);
                candidatesTableView.getItems().clear();
                teamAgentProgressDataTableView.getItems().clear();
                alliesTeamDataTableView.getItems().clear();
                battlefieldNameLabel.setText("");
                numOfAliieTeamsLabel.setText("");
                difficultyLabel.setText("");
                statusLabel.setText("");
                secretMsgLabel.setText("");
                bodyController.cleanDataInDashBoard();
                bodyController.switchTabs();
            });
        }
    }

    public void startCheckIfUBoatLogout() {
        checkLogoutRefresher = new LogoutRefresher(this::logoutConsumer, bodyController.getUboatName());
        logoutTimer = new Timer();
        logoutTimer.schedule(checkLogoutRefresher, 2000, 2000);
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

    @FXML
    void okWinnerButtonHandler(ActionEvent event) {
        logoutTimer.cancel();
        winnerNameLabel.setText("");
        winnerAncorPane.setVisible(false);
        candidatesTableView.getItems().clear();
        teamAgentProgressDataTableView.getItems().clear();
        alliesTeamDataTableView.getItems().clear();
        battlefieldNameLabel.setText("");
        numOfAliieTeamsLabel.setText("");
        difficultyLabel.setText("");
        statusLabel.setText("");
        secretMsgLabel.setText("");
        bodyController.cleanDataInDashBoard();
        bodyController.switchTabs();

        String finalUrl = HttpUrl
                .parse(Constants.OK_CLEAN_PRESS)
                .newBuilder()
                .addQueryParameter("UBoatName", bodyController.getUboatName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
