package subComponent.body;

import dto.AgentResponse;
import dto.CodeConfiguration;
import dto.InputProcess;
import dto.MachineHistory;
import fullApp.FullAppController;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import subComponent.bruteForceTab.BruteForceSettings;
import subComponent.bruteForceTab.BruteForceTabController;
import subComponent.machineTab.MachineTabController;
import subComponent.processTab.ProcessTabController;

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
    private BorderPane processTabComponent;
    @FXML
    private ProcessTabController processTabComponentController;
    @FXML
    private BorderPane bruteForceTabComponent;
    @FXML
    private BruteForceTabController bruteForceTabComponentController;

    @FXML
    public void initialize() {
        if (machineTabComponentController != null && processTabComponentController != null && bruteForceTabComponentController != null) {
            machineTabComponentController.setBodyController(this);
            processTabComponentController.setBodyController(this);
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
        processTabComponentController.createKeyBoard();
    }

    public List<String> getReflectorsList(){
        return mainController.getReflectorIdList();
    }

    public int getRotorCount(){
        return mainController.getRotorCount();
    }

    public void checkRotorListInput(List<Integer> rotorsList) {
        mainController.checkRotorListInput(rotorsList);
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

    public SimpleIntegerProperty getAgentsMaxNumber() {
        return mainController.getAgentsMaxNumber();
    }

    public SimpleLongProperty totalMissionsProperty() {
        return mainController.totalMissionsProperty();
    }

    public SimpleLongProperty totalCompletedMissionsProperty() {
        return mainController.totalCompletedMissionsProperty();
    }
    public SimpleLongProperty totalTimeProperty() {
        return mainController.totalTimeProperty();
    }

    public SimpleStringProperty taskMsgProperty() {
        return mainController.taskMsgProperty();
    }

    public SimpleDoubleProperty taskProgressProperty() {
        return mainController.taskProgressProperty();
    }

    public SimpleStringProperty taskPercentageProperty() {
        return mainController.taskPercentageProperty();
    }

    public SimpleLongProperty avgTimePerMissionProperty() {
        return mainController.avgTimePerMissionProperty();
    }

    public void bindBodyToFullApp() {
        machineTabComponentController.bindMachineToFullApp();
        processTabComponentController.bindProcessTabToFullApp();
        bruteForceTabComponentController.bindBruteForceTabToFullApp();
    }

    public void clearLastMachine() {
        machineTabComponentController.clearLastMachine();
        processTabComponentController.clearLastMachine();
    }

    public CodeConfiguration getCurrentCodeConfiguration() {
        return mainController.getCurrentCodeConfiguration();
    }

    public void setCurrentCodeComponent() {
        machineTabComponentController.setCurrentCodeComponent(getCurrentCodeConfiguration());
        processTabComponentController.setCurrentCodeComponent(getCurrentCodeConfiguration());
    }

    public void initializeToOriginalCode() {
        mainController.initializeToOriginalCode();
    }

    public void processInput(String text) {
        mainController.processInput(text);
    }

    public void setAutoOutputTextField(String output) {
        processTabComponentController.setAutoOutputTextField(output);
    }

    public String processCharacter(String character) {
        return mainController.processCharacter(character);
    }

    public boolean isValidInput(String character) {
        return mainController.isValidInput(character);
    }

    public MachineHistory getMachineHistory() {
        return mainController.getMachineHistory();
    }

    public String getDescriptionOfCurrentSettings(CodeConfiguration currSettings) {
        return mainController.getDescriptionOfCurrentSettings(currSettings);
    }

    public void updateHistory(InputProcess newProcess) {
        mainController.updateHistory(newProcess);
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

    public void startRunningBruteForce(BruteForceSettings bruteForceSettings) {
        mainController.startRunningBruteForce(bruteForceSettings);
    }

    public void createTile(AgentResponse agentResponse) {
        bruteForceTabComponentController.createTile(agentResponse);
    }

    public void cleanOldResultsOfBruteForce() {
        bruteForceTabComponentController.cleanOldResultsOfBruteForce();
    }

    public void cleanBruteForceData() {
        mainController.cleanOldResultsOfBruteForce();
    }

    public void stopBruteForce() {
        mainController.stopBruteForce();
    }

    public void pauseBruteForce() {
        mainController.pauseBruteForce();
    }

    public void ResumeBruteForce() {
        mainController.ResumeBruteForce();
    }

    public void toggleStartStopOnFinish() {
        bruteForceTabComponentController.toggleStartStopOnFinish();
    }

    public void rotateTransitionAnimation() {
        mainController.rotateTransitionAnimation();
    }
}
