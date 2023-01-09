package subComponent.processTab;

import dto.CodeConfiguration;
import dto.InputProcess;
import dto.MachineHistory;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import subComponent.body.BodyController;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import subComponent.currentCode.CurrentCodeController;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

public class ProcessTabController implements Initializable {
    private BodyController bodyController;
    @FXML
    private TextField autoInputTextField;
    @FXML
    private TextField autoOutputTextField;
    @FXML
    private Button processButton;
    @FXML
    private Label invalidInputLabel;
    @FXML
    private Label currentCodeLabel;
    @FXML
    private Button clearButton;
    @FXML
    private TextArea manualInputTextArea;
    @FXML
    private TextArea manualOutputTextArea;
    @FXML
    private Button doneButton;
    @FXML
    private TextArea historyTextArea;
    @FXML
    private FlowPane keyBoardFlowPane;
    @FXML
    private VBox currentCodeComponent;
    @FXML
    private CurrentCodeController currentCodeComponentController;
    @FXML
    private GridPane autoGridPane;
    @FXML
    private VBox manualVbox;

    @FXML
    private HBox autoHBox;
    @FXML
    private RadioButton autoRadioButton;
    @FXML
    private RadioButton manualRadioButton;
    @FXML
    private ToggleGroup tgMode;
    @FXML
    private GridPane keyBoardGridPane;
    @FXML
    private TitledPane inputTitledPane;
    @FXML
    private TitledPane outputTitledPane;
    @FXML
    private CheckBox animationCheckBox;

    Map<Character, Button> keyboardButtons;
    private long timeElapsed = 0;

    @FXML
    public void initialize() {
        if (currentCodeComponentController != null) {
            currentCodeComponentController.setProcessTabController(this);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        manualInputTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                if (s2.isEmpty() || s2.length() <= s.length()) {
                    return;
                }
                String character = String.valueOf(s2.toUpperCase().charAt(s.length()));
                if (bodyController.isValidInput(character)) {
                    long startTime = System.nanoTime();
                    String output = bodyController.processCharacter(character);
                    long endTime = System.nanoTime();
                    timeElapsed = timeElapsed + (endTime - startTime);
                    manualOutputTextArea.appendText(output);
                    showAnimationOutput(output.charAt(0));
                    if (animationCheckBox.isSelected()){
                        doAnimationOnOutput(output.charAt(0));
                    }

                }
            }
        });
        autoHBox.disableProperty().bind(autoRadioButton.selectedProperty().not());
        keyBoardFlowPane.disableProperty().bind(manualRadioButton.selectedProperty().not());
        inputTitledPane.disableProperty().bind(manualRadioButton.selectedProperty().not());
        outputTitledPane.disableProperty().bind(manualRadioButton.selectedProperty().not());
        doneButton.disableProperty().bind(manualRadioButton.selectedProperty().not());
    }

    @FXML
    void initializeToOriginalCodeButtonHandler(ActionEvent event) {
        bodyController.initializeToOriginalCode();
        manualInputTextArea.setText("");
        manualOutputTextArea.setText("");
    }

    @FXML
    void autoClearButtonHandler(ActionEvent event) {
        autoInputTextField.setText("");
        autoOutputTextField.setText("");
        invalidInputLabel.setVisible(false);
    }

    @FXML
    void processButtonHandler(ActionEvent event) {
        String text = autoInputTextField.getText().toUpperCase();
        if (bodyController.isValidInput(text)) {
            bodyController.processInput(text);
            updateMachineHistory();
            if (animationCheckBox.isSelected()){
                rotateTransitionAnimation();
            }
            invalidInputLabel.setVisible(false);
        } else {
            invalidInputLabel.setVisible(true);
        }
    }

    private void updateMachineHistory() {
        historyTextArea.clear();
        MachineHistory machineHistory = bodyController.getMachineHistory();
        for (Map.Entry<CodeConfiguration, List<InputProcess>> entry : machineHistory.getHistoryAndStats().entrySet()) {
            CodeConfiguration currSettings = entry.getKey();
            historyTextArea.appendText(bodyController.getDescriptionOfCurrentSettings(currSettings));
            historyTextArea.appendText(System.lineSeparator());
            List<InputProcess> currProcessList = entry.getValue();
            int i = 1;
            for (InputProcess process : currProcessList) {
                String s = i + ". " + "<" + process.getInput() + "> --> <" + process.getOutput() + "> (" + process.getTime() + " nano-seconds)" + System.lineSeparator();
                historyTextArea.appendText(s);
                i++;
            }
            historyTextArea.appendText(System.lineSeparator());
        }
    }

    @FXML
    void doneButtonHandler(ActionEvent event) {
        InputProcess newProcess = new InputProcess(manualInputTextArea.getText(), manualOutputTextArea.getText(), timeElapsed);
        bodyController.updateHistory(newProcess);
        updateMachineHistory();
        manualOutputTextArea.setText("");
        manualInputTextArea.setText("");
        timeElapsed = 0;
    }


    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    public void setCurrentCodeComponent(CodeConfiguration codeConfiguration) {
        currentCodeComponentController.updateCurrentCodeConfiguration(codeConfiguration);
    }

    public void setAutoOutputTextField(String output) {
        autoOutputTextField.setText(output);
    }

    public void createKeyBoard() {
        String alphabet = bodyController.getAlphabet();
        keyboardButtons = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            Button btn = new Button();
            btn.setPrefHeight(40);
            btn.setPrefWidth(40);
            btn.getStyleClass().add("lampKeyBoard");
            keyboardButtons.put(alphabet.charAt(i), btn);
            btn.setText(String.valueOf(alphabet.charAt(i)));
            btn.setOnAction(event.get());
            keyBoardFlowPane.getChildren().add(btn);
        }
    }

    SimpleObjectProperty<EventHandler<ActionEvent>> event = new SimpleObjectProperty<>(this, "event", new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            Button btn = (Button) e.getSource();
            manualInputTextArea.appendText(btn.getText());
            String output = manualOutputTextArea.getText();
            showAnimationOutput(output.charAt(output.length() - 1));
            if (animationCheckBox.isSelected()){
                doAnimationOnOutput(output.charAt(output.length() - 1));
            }
        }
    });

    private void showAnimationOutput(Character outputKey) {
        Button button = keyboardButtons.get(outputKey);
        final Color startColor = Color.YELLOW;
        final Color endColor = Color.LIGHTGREY;

        final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(startColor);

        // String that represents the color above as a JavaFX CSS function:
        // -fx-body-color: rgb(r, g, b);
        // with r, g, b integers between 0 and 255
        final StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.format("-fx-background-color: rgb(%d, %d, %d);",
                        (int) (256 * color.get().getRed()),
                        (int) (256 * color.get().getGreen()),
                        (int) (256 * color.get().getBlue()));
            }
        }, color);

        // bind the button's style property
        button.styleProperty().bind(cssColorSpec);

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
                new KeyFrame(Duration.seconds(2), new KeyValue(color, endColor)));
        timeline.play();
    }

    private void doAnimationOnOutput(Character outputKey) {
        Button button = keyboardButtons.get(outputKey);
        ScaleTransition st = new ScaleTransition(Duration.millis(600), button);
        st.setByX(1.5f);
        st.setByY(1.5f);
        st.setCycleCount((int) 4f);
        st.setAutoReverse(true);
        st.play();
    }

    private void rotateTransitionAnimation(){
        bodyController.rotateTransitionAnimation();
    }

    public void clearLastMachine() {
        keyBoardFlowPane.getChildren().clear();
        currentCodeComponentController.clearCurrentCode();
        manualOutputTextArea.setText("");
        manualInputTextArea.setText("");
        autoInputTextField.setText("");
        autoOutputTextField.setText("");
        invalidInputLabel.setVisible(false);
        historyTextArea.clear();
        timeElapsed = 0;
    }

    public void bindProcessTabToFullApp() {
        autoGridPane.disableProperty().bind(bodyController.isCodeConfigurationSelected().not());
        manualVbox.disableProperty().bind(bodyController.isCodeConfigurationSelected().not());
        keyBoardGridPane.disableProperty().bind(bodyController.isCodeConfigurationSelected().not());
        currentCodeLabel.textProperty().bind(bodyController.currentCodeConfigurationProperty());
    }
}
