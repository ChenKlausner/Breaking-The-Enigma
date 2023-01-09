package subComponent.body;

import dto.AgentResponse;
import dto.CodeConfiguration;
import dto.InputProcess;
import dto.MachineHistory;
import javafx.application.Platform;
import uboatApp.FullAppController;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import subComponent.bruteForceTab.BruteForceTabController;
import subComponent.machineTab.MachineTabController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BodyController {
    private FullAppController mainController;
    @FXML
    private BorderPane machineTabComponent;
    @FXML
    private MachineTabController machineTabComponentController;

    @FXML
    private BorderPane bruteForceTabComponent;
    @FXML
    private BruteForceTabController bruteForceTabComponentController;

    @FXML
    public void initialize() {
        if (machineTabComponentController != null  && bruteForceTabComponentController != null) {
            machineTabComponentController.setBodyController(this);
            bruteForceTabComponentController.setBodyController(this);
        }
    }

    public void setMainController(FullAppController mainController) {
        this.mainController = mainController;
    }

    public void getMachineCodeConfiguration() {
        mainController.getMachineCodeConfiguration();
    }

    public void setCodeConfigurationAutomatically() {
        mainController.setCodeConfigurationAutomatically();
    }

    public String getAlphabet() {
        return mainController.getAlphabet();
    }

    public String getReflectorInput() {
        return machineTabComponentController.getReflectorInput();
    }

    public LinkedHashMap<Integer, Character> getRotorsToUse() {
        return machineTabComponentController.getRotorsToUse();
    }

    public Map<Character, Character> getPlugInUse() {
        return machineTabComponentController.getPlugInUse();
    }

    public void viewCodeConfigurationSetup(int machineRotorCount, List<String> reflectorIdList) {
        machineTabComponentController.viewCodeConfigurationSetup(machineRotorCount, reflectorIdList);
    }

    public List<String> getReflectorsList(){
        return mainController.getReflectorIdList();
    }

    public int getRotorCount(){
        return mainController.getRotorCount();
    }

    public SimpleBooleanProperty isFileSelectedProperty() {
        return mainController.isFileSelectedProperty();
    }

    public SimpleIntegerProperty possibleAmountOfRotorsProperty() {
        return mainController.possibleAmountOfRotorsProperty();
    }
    public SimpleIntegerProperty amountOfRotorsInUseProperty() {
        return mainController.amountOfRotorsInUseProperty();
    }

    public SimpleIntegerProperty amountOfReflectorsProperty() {
        return mainController.amountOfReflectorsProperty();
    }

    public SimpleIntegerProperty amountOfMassagesProperty() {
        return mainController.amountOfMassagesProperty();
    }

    public SimpleStringProperty originalCodeConfigurationProperty() {
        return mainController.originalCodeConfigurationProperty();
    }

    public SimpleStringProperty currentCodeConfigurationProperty() {
        return mainController.currentCodeConfigurationProperty();
    }

    public SimpleBooleanProperty isCodeConfigurationSelected() {
        return mainController.isCodeConfigurationSelected();
    }

    public void bindBodyToFullApp() {
        machineTabComponentController.bindMachineToFullApp();
        bruteForceTabComponentController.bindBruteForceTabToFullApp();
    }

    public void clearLastMachine() {
        machineTabComponentController.clearLastMachine();
    }

    public CodeConfiguration getCurrentCodeConfiguration() {
        return mainController.getCurrentCodeConfiguration();
    }

    public void setCurrentCodeComponent() {
        machineTabComponentController.setCurrentCodeComponent(getCurrentCodeConfiguration());
    }

    public void initializeToOriginalCode() {
        mainController.initializeToOriginalCode();
    }

    public boolean isValidInput(String character) {
        return mainController.isValidInput(character);
    }

    public String getDescriptionOfCurrentSettings(CodeConfiguration currSettings) {
        return mainController.getDescriptionOfCurrentSettings(currSettings);
    }

    public void initDictionaryInBruteForceTab(Set<String> dictionary) {
        bruteForceTabComponentController.initDictionaryInBruteForceTab(dictionary);
    }

    public boolean isDictionaryContainWordsFromInput(String text) {
        return mainController.isDictionaryContainWordsFromInput(text);
    }

    public String excludeInputFromInvalidCharacters(String text) {
        return mainController.excludeInputFromInvalidCharacters(text);
    }

    public String processInputToBruteForce(String text) {
        return mainController.processInputToBruteForce(text);
    }


    public void startAllieTeamsDataRefresher() {
        bruteForceTabComponentController.startAllieTeamsDataRefresher();
    }

    public void switchToLogin() {
        mainController.switchToLogin();
    }

    public void clearData() {
        machineTabComponentController.clearAllData();
        mainController.cleanAllData();
    }
}
