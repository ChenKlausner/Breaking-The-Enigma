package subComponent.bruteForceTab;

import dto.AgentResponse;
import decryptionManager.DifficultyLevel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import subComponent.body.BodyController;
import subComponent.singleCandidate.SingleCandidateController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static tasks.common.ResourcesConstants.MAIN_FXML_RESOURCE;

public class BruteForceTabController {
    private BodyController bodyController;
    @FXML
    private BorderPane bruteForceBorderPane;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private ComboBox<String> difficultyComboBox;
    @FXML
    private TextField taskSizeTextField;
    @FXML
    private Button startBruteForceButton;
    @FXML
    private Label taskMessageLabel;
    @FXML
    private ProgressBar taskProgressBar;
    @FXML
    private Label progressPercentLabel;
    @FXML
    private FlowPane candidatesFlowPane;
    @FXML
    private Label currentCodeLabel;
    @FXML
    private ListView<String> dictionaryListView;
    @FXML
    private TextField searchWordTextField;
    @FXML
    private Slider agentsNumberSlider;
    @FXML
    private Label invalidInputLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Label totalMissionsLabel;
    @FXML
    private Label totalCompletedMissionsLabel;
    @FXML
    private Button stopBruteForceButton;
    @FXML
    private Button pauseBruteForceButton;
    @FXML
    private Button resumeBruteForceButton;
    @FXML
    private Button clearBruteForceDataButton;
    @FXML
    private HBox controlBruteForceButtonsHBox;
    @FXML
    private Label agentNumberLabel;
    @FXML
    private Label avgTimeLabel;
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    private boolean processSucceeded = false;
    private String secretStringToDm;
    Trie dictionaryTrie;

    @FXML
    public void initialize() {
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard", "Impossible");
        difficultyComboBox.getSelectionModel().selectFirst();
        controlBruteForceButtonsHBox.disableProperty().bind(startBruteForceButton.disableProperty().not());
        resumeBruteForceButton.disableProperty().bind(pauseBruteForceButton.disableProperty().not());
        clearBruteForceDataButton.disableProperty().bind(startBruteForceButton.disableProperty());
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

        agentsNumberSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            agentNumberLabel.setText(new_val.toString());
        });
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
        secretStringToDm = "";
        invalidInputLabel.setVisible(false);
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
            secretStringToDm = outputTextField.getText();
        } else {
            invalidInputLabel.setVisible(true);
        }
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

    @FXML
    void startBruteForceButtonOnActionHandler(ActionEvent event) {
        taskSizeTextField.setStyle("");
        inputTextField.setStyle("");
        outputTextField.setStyle("");
        if (!processSucceeded){
            inputTextField.setStyle(errorStyle);
            outputTextField.setStyle(errorStyle);
        } else if (taskSizeTextField.getText().isEmpty()) {
            taskSizeTextField.setStyle(errorStyle);
        } else if (Integer.valueOf(taskSizeTextField.getText()) <= 0) {
            taskSizeTextField.setStyle(errorStyle);
        } else {
            DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(difficultyComboBox.getValue().toUpperCase());
            Integer taskSize = Integer.valueOf(taskSizeTextField.getText());
            BruteForceSettings bruteForceSettings = new BruteForceSettings(outputTextField.getText(), (int) agentsNumberSlider.getValue(), difficultyLevel, taskSize);
            bodyController.startRunningBruteForce(bruteForceSettings);
            startBruteForceButton.setDisable(true);
        }
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    public void bindBruteForceTabToFullApp() {
        bruteForceBorderPane.disableProperty().bind(bodyController.isCodeConfigurationSelected().not());
        currentCodeLabel.textProperty().bind(bodyController.currentCodeConfigurationProperty());
        agentsNumberSlider.maxProperty().bind(bodyController.getAgentsMaxNumber());
        totalMissionsLabel.textProperty().bind(Bindings.format("%,d", bodyController.totalMissionsProperty()));
        totalCompletedMissionsLabel.textProperty().bind(Bindings.format("%,d", bodyController.totalCompletedMissionsProperty()));
        taskMessageLabel.textProperty().bind(bodyController.taskMsgProperty());
        taskProgressBar.progressProperty().bind(bodyController.taskProgressProperty());
        progressPercentLabel.textProperty().bind(bodyController.taskPercentageProperty());
        avgTimeLabel.textProperty().bind(Bindings.concat(Bindings.format("%,d", bodyController.avgTimePerMissionProperty()), " ns"));
        totalTimeLabel.textProperty().bind(Bindings.concat(Bindings.format("%,d", bodyController.totalTimeProperty()), " ms"));
    }

    public void initDictionaryInBruteForceTab(Set<String> dictionary) {
        dictionaryTrie = new Trie(new ArrayList<>(dictionary));
        ObservableList<String> viewListDict = FXCollections.observableArrayList(dictionaryTrie.suggest(""));
        dictionaryListView.getItems().clear();
        dictionaryListView.setItems(viewListDict);
    }

    public void createTile(AgentResponse agentResponse) {
        for (int i = 0; i < agentResponse.getCandidates().size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MAIN_FXML_RESOURCE);
                Node singleCandidateTile = loader.load();

                SingleCandidateController singleCandidateController = loader.getController();

                singleCandidateController.setCandidateStringLabel(agentResponse.getCandidates().get(i).getDecryptMsg());
                String threadName = agentResponse.getCandidates().get(i).getThreadId().substring(agentResponse.getCandidates().get(i).getThreadId().length() - 9);
                singleCandidateController.setThreadNameLabel(threadName);
                String code = bodyController.getDescriptionOfCurrentSettings(agentResponse.getCandidates().get(i).getCodeConfiguration());
                singleCandidateController.setCodeConfigurationLabel(code);

                candidatesFlowPane.getChildren().add(singleCandidateTile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onActionStopButtonHandler(ActionEvent event) {
        bodyController.stopBruteForce();
    }

    @FXML
    void onActionPauseButtonHandler(ActionEvent event) {
        bodyController.pauseBruteForce();
        pauseBruteForceButton.setDisable(true);
    }

    @FXML
    void onActionResumeButtonHandler(ActionEvent event) {
        bodyController.ResumeBruteForce();
        pauseBruteForceButton.setDisable(false);
    }

    @FXML
    void onClearBruteForceData(ActionEvent event) {
        bodyController.cleanBruteForceData();
        startBruteForceButton.setDisable(false);
    }

    public void cleanOldResultsOfBruteForce() {
        candidatesFlowPane.getChildren().clear();
    }

    public void toggleStartStopOnFinish() {
        startBruteForceButton.setDisable(false);
        pauseBruteForceButton.setDisable(false);
    }
}
