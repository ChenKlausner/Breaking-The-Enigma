package subComponent.dashboard;

import dto.AgentDataDto;
import dto.BattlefieldDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import subComponent.body.BodyController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static util.Constants.REFRESH_RATE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class DashboardTabController {
    private BodyController bodyController;
    @FXML
    private TableView<SingleContest> contestDataTableView;
    @FXML
    private TableColumn<SingleContest, String> battlefieldNameCol;
    @FXML
    private TableColumn<SingleContest, String> uboatUserNameCol;
    @FXML
    private TableColumn<SingleContest, String> statusCol;
    @FXML
    private TableColumn<SingleContest, String> difficultyLevelCol;
    @FXML
    private TableColumn<SingleContest, String> listedTeamsVsNeededTeamsCol;
    @FXML
    private Button signButton;
    @FXML
    private Label signInfoLabel;
    @FXML
    private TableView<SingleAgent> teamsAgentDataTableView;
    @FXML
    private TableColumn<SingleAgent, String> agentUserNameCol;
    @FXML
    private TableColumn<SingleAgent, String> amountOfThreadsCol;
    @FXML
    private TableColumn<SingleAgent, String> amountOfTasks;
    @FXML
    private TextField allieTaskSizeTextField;
    @FXML
    private Button readyButton;
    @FXML
    private Label invalidReadyLabel;

    private Timer timer;
    private TimerTask contestDataRefresher;
    private Timer agentTeamTimer;
    private TimerTask agentTeamDataRefresher;
    private ObservableList<SingleContest> singleContestDataList;
    private String prevSelectedContest;
    private String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    private Timer contestIsReadyTimer;
    private TimerTask contestIsReadyRefresher;


    @FXML
    public void initialize() {
        battlefieldNameCol.setCellValueFactory(new PropertyValueFactory<SingleContest, String>("battleFieldName"));
        uboatUserNameCol.setCellValueFactory(new PropertyValueFactory<SingleContest, String>("uBoatUserName"));
        statusCol.setCellValueFactory(new PropertyValueFactory<SingleContest, String>("status"));
        difficultyLevelCol.setCellValueFactory(new PropertyValueFactory<SingleContest, String>("difficultyLevel"));
        listedTeamsVsNeededTeamsCol.setCellValueFactory(new PropertyValueFactory<SingleContest, String>("listedTeamsVsNeededTeams"));
        contestDataTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                prevSelectedContest = newValue.getBattleFieldName();
            }
        }));

        agentUserNameCol.setCellValueFactory(new PropertyValueFactory<SingleAgent, String>("agentUserName"));
        amountOfThreadsCol.setCellValueFactory(new PropertyValueFactory<SingleAgent, String>("amountOfThreads"));
        amountOfTasks.setCellValueFactory(new PropertyValueFactory<SingleAgent, String>("amountOfTasks"));
    }

    @FXML
    void allieReadyButtonOnActionHandler(ActionEvent event) {
        invalidReadyLabel.setText("");
        if (allieTaskSizeTextField.getText().isEmpty()) {
            allieTaskSizeTextField.setStyle(errorStyle);
        } else if (Integer.valueOf(allieTaskSizeTextField.getText()) <= 0) {
            allieTaskSizeTextField.setStyle(errorStyle);
        } else if (teamsAgentDataTableView.getItems().size() < 1) {
            invalidReadyLabel.setText("Allie not ready, you must have at least 1 agent in your team!");
        } else {
            String finalUrl = HttpUrl
                    .parse(Constants.ALLIE_READY)
                    .newBuilder()
                    .addQueryParameter("taskSize", allieTaskSizeTextField.getText())
                    .addQueryParameter("uBoatUserName", bodyController.getUboatName())
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .build();

            Call call = HTTP_CLIENT.newCall(request);
            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    startCheckIfContestIsReadyRefresher();
                    bodyController.startTeamsAgentsProgress();
                    bodyController.startTakingCandidates();
                    invalidReadyLabel.setText("Allie is ready!");
                    readyButton.setDisable(true);
                    bodyController.startCheckWinner();
                } else {
                    invalidReadyLabel.setText("Allie not ready, you must have at least 1 agent in your team!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    private void updateContestDataTableView(List<BattlefieldDto> battlefieldDtoList) {
        Platform.runLater(() -> {
            singleContestDataList = FXCollections.observableArrayList();
            for (BattlefieldDto battlefieldDto : battlefieldDtoList) {
                singleContestDataList.add(new SingleContest(battlefieldDto));
            }
            contestDataTableView.getItems().clear();
            contestDataTableView.setItems(singleContestDataList);
            Optional<SingleContest> lastSelectedOption = contestDataTableView.getItems()
                    .filtered(singleContest -> singleContest.getBattleFieldName().equals(prevSelectedContest))
                    .stream().findFirst();
            lastSelectedOption.ifPresent(singleContest -> contestDataTableView.getSelectionModel().select(singleContest));
        });
    }

    public void startContestDataRefresher() {
        contestDataRefresher = new ContestDataRefresher(this::updateContestDataTableView);
        timer = new Timer();
        timer.schedule(contestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML
    void signButtonOnActionHandler(ActionEvent event) {
        signInfoLabel.setText("");
        if (contestDataTableView.getSelectionModel().getSelectedItem() == null) {
            signInfoLabel.setText("Please select contest from below and then press sign!");
            return;
        }

        SingleContest singleContest = contestDataTableView.getSelectionModel().getSelectedItem();
        String finalUrl = HttpUrl
                .parse(Constants.SIGN_TO_CONTEST)
                .newBuilder()
                .addQueryParameter("uBoatUserName", singleContest.getuBoatUserName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();
            if (response.code() != 200) {
                signInfoLabel.setText("Failed sign in! current contest already full!");
            } else {
                readyButton.setDisable(false);
                bodyController.startUpdateContestData();
                bodyController.setUboatName(singleContest.getuBoatUserName());
                bodyController.startContestRefresher();
                signButton.setDisable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAgentTeamsTableView(List<AgentDataDto> agentDataDtoList) {
        Platform.runLater(() -> {
            ObservableList<SingleAgent> agentTeamsData = FXCollections.observableArrayList();
            for (AgentDataDto agentDataDto : agentDataDtoList) {
                agentTeamsData.add(new SingleAgent(agentDataDto.getUserName(), agentDataDto.getNumOfThreads(), agentDataDto.getNumOfTasks()));
            }
            teamsAgentDataTableView.getItems().clear();
            teamsAgentDataTableView.setItems(agentTeamsData);
        });
    }

    public void startAgentTeamsDataRefresher() {
        agentTeamDataRefresher = new TeamsAgentRefresher(this::updateAgentTeamsTableView);
        agentTeamTimer = new Timer();
        agentTeamTimer.schedule(agentTeamDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void isReadyConsumer(boolean isReady) {
        Platform.runLater(() -> {
            if (isReady) {
                startContest();
                contestIsReadyTimer.cancel();
            }
        });
    }

    public void startCheckIfContestIsReadyRefresher() {
        contestIsReadyRefresher = new ContestIsReadyRefresher(this::isReadyConsumer, bodyController.getUboatName());
        contestIsReadyTimer = new Timer();
        contestIsReadyTimer.schedule(contestIsReadyRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void startContest() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed starting dm");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.START_DM)
                .newBuilder()
                .addQueryParameter("uboatName", bodyController.getUboatName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public void cleanData() {
        signButton.setDisable(false);
        readyButton.setDisable(true);
        allieTaskSizeTextField.setText("");
        invalidReadyLabel.setText("");
    }
}
