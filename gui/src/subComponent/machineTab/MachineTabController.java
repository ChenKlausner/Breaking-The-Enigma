package subComponent.machineTab;

import dto.CodeConfiguration;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import myException.IdIsNotUniqueException;
import subComponent.body.BodyController;
import subComponent.currentCode.CurrentCodeController;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MachineTabController {
    private BodyController bodyController;
    @FXML
    private Label rotorInUseLabel;
    @FXML
    private Label amountOfReflectorLabel;
    @FXML
    private Label messageCounterLabel;
    @FXML
    private Label originalCodeLabel;
    @FXML
    private Label currentCodeLabel;
    @FXML
    private HBox rotorInUseHBox;
    @FXML
    private Button codeConfigurationButton;
    @FXML
    private Button randomCodeButton;
    @FXML
    private Button clearConfigurationButton;
    @FXML
    private ComboBox<String> reflectorComboBox;
    @FXML
    private Text reflectorTextErrorMsg;
    @FXML
    private Text rotorsTextErrorMsg;
    @FXML
    private FlowPane plugBoardFlowPane;

    Button firstBtn = null;
    String lastColor = null;
    boolean isFirstButton = true;
    Map<Button, Button> plugInUse = new HashMap<>();

    @FXML
    private VBox currentCodeComponent;
    @FXML
    private CurrentCodeController currentCodeComponentController;

    @FXML
    public void initialize() {
        if (currentCodeComponentController!= null) {
            currentCodeComponentController.setMachineTabController(this);
        }
    }

    public void setBodyController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    @FXML
    void setCodeConfigurationButtonHandler(ActionEvent event) {
        reflectorTextErrorMsg.setVisible(false);
        rotorsTextErrorMsg.setVisible(false);
        bodyController.getMachineCodeConfiguration();
    }

    @FXML
    void randomCodeButtonPressedHandler(ActionEvent event) {
        bodyController.setCodeConfigurationAutomatically();
        currentCodeComponentController.updateCurrentCodeConfiguration(bodyController.getCurrentCodeConfiguration());
    }

    @FXML
    void clearSelectionEventHandler(ActionEvent event) {
        reflectorComboBox.getItems().clear();
        rotorInUseHBox.getChildren().clear();
        plugBoardFlowPane.getChildren().clear();
        firstBtn = null;
        lastColor = null;
        isFirstButton = true;
        plugInUse = new HashMap<>();
        viewCodeConfigurationSetup(bodyController.getRotorCount(),bodyController.getReflectorsList());
    }

    public void createPlugBoard() {
        String alphabet = bodyController.getAlphabet();
        for (int i = 0; i < alphabet.length(); i++) {
            Button btn = new Button();
            btn.setText(String.valueOf(alphabet.charAt(i)));
            btn.setOnAction(event.get());
            plugBoardFlowPane.getChildren().add(btn);
        }
    }

    SimpleObjectProperty<EventHandler<ActionEvent>> event = new SimpleObjectProperty<>(this, "event", new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            Button btn = (Button) e.getSource();
            for (Map.Entry<Button, Button> entry : plugInUse.entrySet()) {
                if (entry.getKey() == btn || entry.getValue() == btn) {
                    entry.getKey().setStyle(null);
                    entry.getValue().setStyle(null);
                    plugInUse.remove(entry.getKey());
                    isFirstButton = true;
                    return;
                }
            }
            if (firstBtn == btn) {
                btn.setStyle(null);
                firstBtn = null;
                isFirstButton = true;
                return;
            }
            if (isFirstButton) {
                Random rand = new Random();
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                Color randomColor = new Color(r, g, b, 1);
                String color = "-fx-background-color: " + randomColor.toString().replace("0x", "#");
                btn.setStyle(color);
                firstBtn = btn;
                lastColor = color;
                isFirstButton = false;
            } else {
                btn.setStyle(lastColor);
                plugInUse.put(firstBtn, btn);
                isFirstButton = true;
            }
        }
    });

    public Map<Character, Character> getPlugInUse() {
        Map<Character, Character> plugsInUse = new HashMap<>();
        for (Map.Entry<Button, Button> entry : plugInUse.entrySet()) {
            plugsInUse.put(entry.getKey().getText().charAt(0), entry.getValue().getText().charAt(0));
        }
        return plugsInUse;
    }

    public void createRotorsSetupOptions(int rotorCount) {
        List<String> posOptions;
        String alphabet = bodyController.getAlphabet();
        posOptions = IntStream.range(0, alphabet.length()).mapToObj(i -> String.valueOf(alphabet.charAt(i))).collect(Collectors.toList());
        ObservableList<String> positionOptions = FXCollections.observableArrayList(posOptions);
        List<Integer> rotorIdOption = IntStream.range(0, bodyController.possibleAmountOfRotorsProperty().get()).mapToObj(i1 -> i1 + 1).collect(Collectors.toList());
        ObservableList<Integer> rotorsOptions = FXCollections.observableArrayList(rotorIdOption);
        for (int i = 0; i < rotorCount; i++) {
            rotorInUseHBox.getChildren().add(createComponentOfRotorAndPositionVbox(rotorCount - i, rotorsOptions, positionOptions));
        }
    }

    public Node createRotorVbox(int rotorId, ObservableList<Integer> rotorOptions) {
        VBox vBox = new VBox();
        vBox.setPrefHeight(75);
        vBox.setPrefWidth(100);
        vBox.setSpacing(5);
        Label label = new Label("Rotor " + rotorId);
        ComboBox comboBox = new ComboBox<>(rotorOptions);
        comboBox.setPrefWidth(150);
        vBox.getChildren().add(label);
        vBox.getChildren().add(comboBox);
        return vBox;
    }

    public Node createPositionVbox(ObservableList<String> positionOptions) {
        VBox vBox = new VBox();
        vBox.setPrefHeight(75);
        vBox.setPrefWidth(100);
        vBox.setSpacing(5);
        Label label = new Label("POSITION");
        ComboBox comboBox = new ComboBox<>(positionOptions);
        comboBox.setPrefWidth(150);
        vBox.getChildren().add(label);
        vBox.getChildren().add(comboBox);
        return vBox;
    }

    public Node createComponentOfRotorAndPositionVbox(int rotorId, ObservableList<Integer> rotorOptions, ObservableList<String> positionOptions) {
        VBox vBox = new VBox();
        vBox.setMinWidth(85);
        vBox.setMinHeight(120);
        vBox.setPrefHeight(85);
        vBox.setPrefWidth(120);
        vBox.setSpacing(10);
        vBox.getChildren().add(createRotorVbox(rotorId, rotorOptions));
        vBox.getChildren().add(createPositionVbox(positionOptions));
        return vBox;
    }

    private void setReflectorComboBox(List<String> reflectorList) {
        IntStream.range(0, reflectorList.size()).forEachOrdered(i -> reflectorComboBox.getItems().add(reflectorList.get(i)));
    }

    public void viewCodeConfigurationSetup(int rotorCount, List<String> reflectorList) {
        createRotorsSetupOptions(rotorCount);
        setReflectorComboBox(reflectorList);
        createPlugBoard();
    }

    public String getReflectorInput() {
        if (reflectorComboBox.getSelectionModel().isEmpty()) {
            reflectorTextErrorMsg.setVisible(true);
            return null;
        }
        return reflectorComboBox.getValue();
    }

    public LinkedHashMap<Integer, Character> getRotorsToUse() {
        List<Integer> rotorsList = new ArrayList<>();
        List<Character> startPosition = new ArrayList<>();

        for (Node child : rotorInUseHBox.getChildren()) {
            VBox vBox = (VBox) child;
            VBox vBoxRotor = (VBox) vBox.getChildren().get(0);
            VBox vBoxPosition = (VBox) vBox.getChildren().get(1);
            ComboBox comboRotorId = (ComboBox) vBoxRotor.getChildren().get(1);
            ComboBox comboPosition = (ComboBox) vBoxPosition.getChildren().get(1);
            if (comboRotorId.getSelectionModel().isEmpty() || comboPosition.getSelectionModel().isEmpty()) {
                rotorsTextErrorMsg.setText("Rotor Id and/or Starting position are required!");
                rotorsTextErrorMsg.setVisible(true);
                return null;
            }
            Integer rotorId = (Integer) comboRotorId.getValue();
            String position = (String) comboPosition.getValue();
            rotorsList.add(0, rotorId);
            startPosition.add(0, position.charAt(0));
        }
        try {
            bodyController.checkRotorListInput(rotorsList);
        } catch (IdIsNotUniqueException e) {
            rotorsTextErrorMsg.setText("Rotors need to be unique");
            rotorsTextErrorMsg.setVisible(true);
            return null;
        }
        LinkedHashMap<Integer, Character> rotorsToUse = IntStream.range(0, rotorsList.size()).boxed().collect(Collectors.toMap(rotorsList::get, startPosition::get, (a, b) -> b, LinkedHashMap::new));
        return rotorsToUse;
    }

    public void bindMachineToFullApp() {
        codeConfigurationButton.disableProperty().bind(bodyController.isFileSelectedProperty().not());
        randomCodeButton.disableProperty().bind(bodyController.isFileSelectedProperty().not());
        clearConfigurationButton.disableProperty().bind(bodyController.isFileSelectedProperty().not());
        messageCounterLabel.textProperty().bind(Bindings.format("%d", bodyController.amountOfMassagesProperty()));
        amountOfReflectorLabel.textProperty().bind(Bindings.format("%d", bodyController.amountOfReflectorsProperty()));
        rotorInUseLabel.textProperty().bind(Bindings.concat(Bindings.format("%d", bodyController.amountOfRotorsInUseProperty()), "\\", Bindings.format("%d", bodyController.possibleAmountOfRotorsProperty())));
        originalCodeLabel.textProperty().bind(bodyController.originalCodeConfigurationProperty());
        currentCodeLabel.textProperty().bind(bodyController.currentCodeConfigurationProperty());
    }

    public void clearLastMachine() {
        reflectorComboBox.getItems().clear();
        rotorInUseHBox.getChildren().clear();
        plugBoardFlowPane.getChildren().clear();
        firstBtn = null;
        lastColor = null;
        isFirstButton = true;
        plugInUse = new HashMap<>();
        currentCodeComponentController.clearCurrentCode();
    }

    public CodeConfiguration getCurrentCodeConfiguration() {
        return bodyController.getCurrentCodeConfiguration();
    }

    public void setCurrentCodeComponent(CodeConfiguration codeConfiguration) {
        currentCodeComponentController.updateCurrentCodeConfiguration(codeConfiguration);
    }
}
