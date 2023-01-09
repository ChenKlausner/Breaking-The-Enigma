package subComponent.bruteForceTab;

import dto.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import subComponent.body.BodyController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.REFRESH_RATE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class BruteForceTabController {
    private BodyController bodyController;
    @FXML
    private BorderPane bruteForceBorderPane;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private FlowPane candidatesFlowPane;
    @FXML
    private Label currentCodeLabel;
    @FXML
    private ListView<String> dictionaryListView;
    @FXML
    private TextField searchWordTextField;
    @FXML
    private Label invalidInputLabel;
    @FXML
    private TableView<SingleAllie> alliesTeamDataTableView;
    @FXML
    private TableColumn<SingleAllie, String> teamNameCol;
    @FXML
    private TableColumn<SingleAllie, Integer> numOfAgentCol;
    @FXML
    private TableColumn<SingleAllie, Integer> taskSizeCol;
    @FXML
    private Button uBoatReadyButton;
    @FXML
    private Label readyMsgLabel;
    @FXML
    private TableView<SingleCandidate> candidatesTableview;
    @FXML
    private TableColumn<SingleCandidate, String> candidateCol;
    @FXML
    private TableColumn<SingleCandidate, String> allieNameCol;
    @FXML
    private TableColumn<SingleCandidate, String> codeConfigurationCol;
    @FXML
    private HBox processInputHbox;
    @FXML
    private Button logoutButton;

    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    private boolean processSucceeded = false;
    Trie dictionaryTrie;
    private Timer allieTeamTimer;
    private TimerTask allieTeamDataRefresher;
    private Timer candidatesResponseTimer;
    private TimerTask takingResponseRefresher;
    @FXML
    private AnchorPane winnerAncorPane;
    @FXML
    private Label winnerNameLabel;

    @FXML
    public void initialize() {
        searchWordTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                ObservableList<String> viewListDict = FXCollections.observableArrayList(dictionaryTrie.suggest(newVal));
                dictionaryListView.getItems().clear();
                dictionaryListView.setItems(viewListDict);
            }
        });

        dictionaryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() % 2 == 0) {
                    String currentItemSelected = dictionaryListView.getSelectionModel().getSelectedItem();
                    inputTextField.appendText(currentItemSelected);
                }
            }
        });

        teamNameCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, String>("allieUserName"));
        numOfAgentCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, Integer>("numOfAgents"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<SingleAllie, Integer>("taskSize"));

        allieNameCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("allieName"));
        candidateCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("decryptMsg"));
        codeConfigurationCol.setCellValueFactory(new PropertyValueFactory<SingleCandidate, String>("codeConfiguration"));
    }

    @FXML
    void uBoatReadyButtonOnAction(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_READY)
                .newBuilder()
                .addQueryParameter("inputMsg", inputTextField.getText())
                .addQueryParameter("secretMsg", outputTextField.getText())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                readyMsgLabel.setText("UBoat is ready!");
                uBoatReadyButton.setDisable(true);
                processInputHbox.setDisable(true);
                logoutButton.setDisable(true);
                startTakingCandidates();
            } else {
                readyMsgLabel.setText("UBoat is not ready please try again after all teams signup");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initToOriginalCodeConfigurationHandler(ActionEvent event) {
        bodyController.initializeToOriginalCode();
    }

    @FXML
    void outputClearButtonOnActionHandler(ActionEvent event) {
        inputTextField.setText("");
        outputTextField.setText("");
        processSucceeded = false;
        invalidInputLabel.setVisible(false);
        uBoatReadyButton.setDisable(true);
    }

    @FXML
    void processButtonOnActionHandler(ActionEvent event) {
        invalidInputLabel.setVisible(false);
        inputTextField.setStyle("");
        outputTextField.setStyle("");
        if (isDictionaryContainWordsFromInput(inputTextField.getText()) && bodyController.isValidInput(inputTextField.getText())) {
            String excludedInput = excludeInputFromInvalidCharacters(inputTextField.getText());
            inputTextField.setText(excludedInput);
            outputTextField.setText(processInputToBruteForce(inputTextField.getText()));
            processSucceeded = true;
            uBoatReadyButton.setDisable(false);
        } else {
            invalidInputLabel.setVisible(true);
        }
    }

    @FXML
    void logoutButtonHandler(ActionEvent event) {
        allieTeamTimer.cancel();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    winnerNameLabel.setText("");
                    winnerAncorPane.setVisible(false);
                    candidatesTableview.getItems().clear();
                    inputTextField.setText("");
                    outputTextField.setText("");
                    uBoatReadyButton.setDisable(true);
                    readyMsgLabel.setText("");
                });
                logoutButton.setDisable(true);
                bodyController.clearData();
                bodyController.switchToLogin();
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }


    private String processInputToBruteForce(String text) {
        return bodyController.processInputToBruteForce(text.toUpperCase());
    }

    private String excludeInputFromInvalidCharacters(String text) {
        return bodyController.excludeInputFromInvalidCharacters(text);
    }

    private boolean isDictionaryContainWordsFromInput(String text) {
        return bodyController.isDictionaryContainWordsFromInput(text);
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    public void bindBruteForceTabToFullApp() {
        bruteForceBorderPane.disableProperty().bind(bodyController.isCodeConfigurationSelected().not());
        currentCodeLabel.textProperty().bind(bodyController.currentCodeConfigurationProperty());
    }

    public void initDictionaryInBruteForceTab(Set<String> dictionary) {
        dictionaryTrie = new Trie(new ArrayList<>(dictionary));
        ObservableList<String> viewListDict = FXCollections.observableArrayList(dictionaryTrie.suggest(""));
        dictionaryListView.getItems().clear();
        dictionaryListView.setItems(viewListDict);
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
        allieTeamDataRefresher = new AlliesTeamDataRefresher(this::updateAllieTeamsTableView);
        allieTeamTimer = new Timer();
        allieTeamTimer.schedule(allieTeamDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public synchronized void uBoatResponseConsumer(UBoatResponse uBoatResponse) {
        if (uBoatResponse != null) {
            for (int i = 0; i < uBoatResponse.getCandidates().size(); i++) {
                String allieName = uBoatResponse.getAllieUserName();
                String candidate = uBoatResponse.getCandidates().get(i).getDecryptMsg();
                String codeConfiguration = getDescriptionOfCurrentSettings(uBoatResponse.getCandidates().get(i).getCodeConfiguration());
                Platform.runLater(() -> {
                    candidatesTableview.getItems().add(new SingleCandidate(allieName, candidate, codeConfiguration));
                });
            }
            if (uBoatResponse.isWinner()) {
                doWhenFoundWinner(uBoatResponse.getAllieUserName());
                processInputHbox.setDisable(false);
                logoutButton.setDisable(false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                candidatesResponseTimer.cancel();
            }
        }
    }

    public void startTakingCandidates() {
        takingResponseRefresher = new TakingResponseRefresher(this::uBoatResponseConsumer);
        candidatesResponseTimer = new Timer();
        candidatesResponseTimer.schedule(takingResponseRefresher, 1000, 1000);
    }

    public void doWhenFoundWinner(String winnerName) {
        Platform.runLater(() -> {
            winnerAncorPane.setVisible(true);
            winnerNameLabel.setText(winnerName);
        });

        /*Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.WINNER_FOUND)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);*/
    }

    @FXML
    void okWinnerButtonHandler(ActionEvent event) {
        winnerNameLabel.setText("");
        winnerAncorPane.setVisible(false);
        candidatesTableview.getItems().clear();
        inputTextField.setText("");
        outputTextField.setText("");
        uBoatReadyButton.setDisable(true);
        readyMsgLabel.setText("");

        String finalUrl = HttpUrl
                .parse(Constants.OK_UBOAT_PRESS)
                .newBuilder()
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
