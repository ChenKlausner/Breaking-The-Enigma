package subComponent.currentCode;


import dto.CodeConfiguration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import subComponent.machineTab.MachineTabController;
import subComponent.processTab.ProcessTabController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CurrentCodeController {
    private MachineTabController machineTabController;
    private ProcessTabController processTabController;
    @FXML
    private FlowPane plugBoardBorderPane;
    @FXML
    private ListView<Integer> rotorIdViewList;
    @FXML
    private ListView<Character> positionsViewList;
    @FXML
    private ListView<Integer> notchViewList;
    @FXML
    private TextField reflectorTextField;

    public void setMachineTabController(MachineTabController machineTabController) {
        this.machineTabController = machineTabController;
    }

    public void setProcessTabController(ProcessTabController processTabController) {
        this.processTabController = processTabController;
    }

    public void updateCurrentCodeConfiguration(CodeConfiguration currentCode) {
        List<Integer> rotorsId = new ArrayList<>();
        List<Character> positions = new ArrayList<>();
        List<Integer> notch = new ArrayList<>();
        Set<Integer> keys = currentCode.getRotorsInUse().keySet();
        keys.forEach(key -> {
            rotorsId.add(key);
            positions.add(currentCode.getRotorsInUse().get(key));
            notch.add(currentCode.getNotchDistanceFromWindow().get(key));
        });
        ObservableList<Integer> currentRotors = FXCollections.observableArrayList(rotorsId);
        ObservableList<Character> currentPositions = FXCollections.observableArrayList(positions);
        ObservableList<Integer> currentNotches = FXCollections.observableArrayList(notch);

        rotorIdViewList.setItems(currentRotors);
        positionsViewList.setItems(currentPositions);
        notchViewList.setItems(currentNotches);
        reflectorTextField.setText(currentCode.getReflectorId());
        plugBoardBorderPane.getChildren().clear();
        currentCode.getPlugsInUse().entrySet().stream().map(entry -> new Label(entry.getKey().toString() + entry.getValue().toString())).forEachOrdered(label -> plugBoardBorderPane.getChildren().add(label));
    }

    public void clearCurrentCode() {
        plugBoardBorderPane.getChildren().clear();
        reflectorTextField.setText("");
        rotorIdViewList.getItems().clear();
        positionsViewList.getItems().clear();
        notchViewList.getItems().clear();
    }
}
