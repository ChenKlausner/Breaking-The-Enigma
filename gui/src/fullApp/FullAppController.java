package fullApp;

import dto.AgentResponse;
import dto.CodeConfiguration;
import dto.InputProcess;
import dto.MachineHistory;
import dto.Specification;
import engine.EngineUtility;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import myException.*;
import subComponent.body.BodyController;
import subComponent.bruteForceTab.BruteForceSettings;
import subComponent.header.HeaderController;
import tasks.BruteForceTask;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FullAppController {
    private EngineUtility engine;
    @FXML
    private GridPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private TabPane bodyComponent;
    @FXML
    private BodyController bodyComponentController;
    @FXML
    private BorderPane fullAppBoarderPane;
    @FXML
    private ScrollPane fullAppScrollPane;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private SimpleIntegerProperty possibleAmountOfRotors;
    private SimpleIntegerProperty amountOfRotorsInUse;
    private SimpleIntegerProperty amountOfReflectors;
    private SimpleIntegerProperty amountOfMassages;
    private SimpleStringProperty originalCodeConfiguration;
    private SimpleStringProperty currentCodeConfiguration;
    private SimpleBooleanProperty isCodeConfigurationSelected;
    private SimpleIntegerProperty agentsMaxNumber;
    private SimpleLongProperty totalMissions;
    private SimpleLongProperty totalCompletedMissions;
    private SimpleLongProperty totalTime;
    private SimpleStringProperty taskMsg;
    private SimpleDoubleProperty taskProgress;
    private SimpleStringProperty taskPercentage;
    private SimpleLongProperty avgTimePerMission;
    private BruteForceTask currentRunningTask;


    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
        }
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        possibleAmountOfRotors = new SimpleIntegerProperty(0);
        amountOfRotorsInUse = new SimpleIntegerProperty(0);
        amountOfReflectors = new SimpleIntegerProperty(0);
        amountOfMassages = new SimpleIntegerProperty(0);
        originalCodeConfiguration = new SimpleStringProperty();
        currentCodeConfiguration = new SimpleStringProperty();
        isCodeConfigurationSelected = new SimpleBooleanProperty(false);
        agentsMaxNumber = new SimpleIntegerProperty(50);
        totalMissions = new SimpleLongProperty(0);
        totalCompletedMissions = new SimpleLongProperty(0);
        totalTime = new SimpleLongProperty();
        taskMsg = new SimpleStringProperty();
        taskProgress = new SimpleDoubleProperty(0);
        taskPercentage = new SimpleStringProperty();
        avgTimePerMission = new SimpleLongProperty();
        headerComponentController.bindHeaderToFullApp();
        bodyComponentController.bindBodyToFullApp();
    }

    public void setEngine(EngineUtility engine) {
        this.engine = engine;
    }

    public void readMachineData(String absolutePath) {
        try {
            engine.loadMachineData(absolutePath);
            selectedFileProperty.set(absolutePath);
            isFileSelected.set(true);
            isCodeConfigurationSelected.set(false);
            setSpecification();
            agentsMaxNumber.set(engine.getNumOfAgents());
            initDictionaryInBruteForceTab(engine.getDictionary());
            clearLastMachine();
            bodyComponentController.viewCodeConfigurationSetup(engine.getMachineRotorCount(), getReflectorIdList());
        } catch (JAXBException | FileNotFoundException e) {
            popErrorMsg("File with the specified pathname does not exist!");
        } catch (SuffixIsNotXmlException e) {
            popErrorMsg(e.getMessage());
        } catch (AlphabetNotEvenLengthException e) {
            popErrorMsg(e.getMessage());
        } catch (InvalidRotorCountException e) {
            popErrorMsg(e.getMessage());
        } catch (NotEnoughRotorsException e) {
            popErrorMsg(e.getMessage());
        } catch (RotorHasDoubleMappingException e) {
            popErrorMsg(e.getMessage());
        } catch (IdIsNotUniqueException e) {
            popErrorMsg(e.getMessage());
        } catch (IdIsNotOrderInSequenceFromOneException e) {
            popErrorMsg(e.getMessage());
        } catch (NotchIsOutOfRangeException e) {
            popErrorMsg(e.getMessage());
        } catch (CharMapToItselfException e) {
            popErrorMsg(e.getMessage());
        } catch (ReflrctorsIdOrderIsNotSequenceOfRomanException e) {
            popErrorMsg(e.getMessage());
        } catch (InvalidNumOfAgentsException e) {
            popErrorMsg(e.getMessage());
        }
    }

    private void initDictionaryInBruteForceTab(Set<String> dictionary) {
        bodyComponentController.initDictionaryInBruteForceTab(dictionary);
    }

    public void getMachineCodeConfiguration() {
        Map<Character, Character> plugs = bodyComponentController.getPlugInUse();
        String reflectorId = bodyComponentController.getReflectorInput();
        LinkedHashMap<Integer, Character> rotorsInUse = bodyComponentController.getRotorsToUse();
        if (reflectorId != null && rotorsInUse != null) {
            CodeConfiguration codeConfiguration = new CodeConfiguration(rotorsInUse, null, reflectorId, plugs);
            engine.setMachineSettingsManually(codeConfiguration);
            setOriginalAndCurrentCodeConfiguration();
            isCodeConfigurationSelected.set(true);
        }
    }

    public void setCodeConfigurationAutomatically() {
        CodeConfiguration codeConfiguration = engine.setMachineSettingsAutomatically();
        setOriginalAndCurrentCodeConfiguration();
        isCodeConfigurationSelected.set(true);
    }

    private void setSpecification() {
        Specification sp = engine.getMachineSpecification();
        this.possibleAmountOfRotors.set(sp.getPossibleAmountOfRotors());
        this.amountOfRotorsInUse.set(sp.getAmountOfRotorsInUse());
        this.amountOfReflectors.set(sp.getAmountOfReflectors());
        this.amountOfMassages.set(sp.getAmountOfMassages());
    }

    private void setOriginalAndCurrentCodeConfiguration() {
        originalCodeConfiguration.set(getDescriptionOfCurrentSettings(engine.getOriginalCodeConfig()));
        currentCodeConfiguration.set(getDescriptionOfCurrentSettings(engine.getCurrentCodeConfig()));
        bodyComponentController.setCurrentCodeComponent();
    }

    public MachineHistory getMachineHistory() {
        return engine.getMachineHistoryAndStatistics();
    }

    public void processInput(String input) {
        String output = engine.encryptOrDecryptInput(input);
        bodyComponentController.setAutoOutputTextField(output);
        setSpecification();
        setOriginalAndCurrentCodeConfiguration();
    }

    public List<String> getReflectorIdList() {
        return IntStream.range(0, engine.getMachineReflectorList().size()).mapToObj(i -> engine.getReflectorId(i)).collect(Collectors.toList());
    }

    public String getAlphabet() {
        return engine.getMachineAlphabet();
    }

    public void checkRotorListInput(List<Integer> rotorsList) {
        engine.checkRotorListInput(rotorsList);
    }

    public SimpleBooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    public SimpleStringProperty selectedFilePropertyProperty() {
        return selectedFileProperty;
    }

    public SimpleIntegerProperty possibleAmountOfRotorsProperty() {
        return possibleAmountOfRotors;
    }

    public SimpleIntegerProperty amountOfRotorsInUseProperty() {
        return amountOfRotorsInUse;
    }

    public SimpleIntegerProperty amountOfReflectorsProperty() {
        return amountOfReflectors;
    }

    public SimpleIntegerProperty amountOfMassagesProperty() {
        return amountOfMassages;
    }

    public SimpleStringProperty originalCodeConfigurationProperty() {
        return originalCodeConfiguration;
    }

    public SimpleStringProperty currentCodeConfigurationProperty() {
        return currentCodeConfiguration;
    }

    public SimpleBooleanProperty isCodeConfigurationSelected() {
        return isCodeConfigurationSelected;
    }

    public SimpleIntegerProperty getAgentsMaxNumber() {
        return agentsMaxNumber;
    }

    public SimpleStringProperty taskMsgProperty() {
        return taskMsg;
    }

    public SimpleDoubleProperty taskProgressProperty() {
        return taskProgress;
    }

    public SimpleStringProperty taskPercentageProperty() {
        return taskPercentage;
    }

    public SimpleLongProperty avgTimePerMissionProperty() {
        return avgTimePerMission;
    }
    private void clearLastMachine() {
        originalCodeConfiguration.set("");
        currentCodeConfiguration.set("");
        bodyComponentController.clearLastMachine();
    }

    public CodeConfiguration getCurrentCodeConfiguration() {
        return engine.getCurrentCodeConfig();
    }

    private void popErrorMsg(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File loading error");
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    public void initializeToOriginalCode() {
        engine.setMachineToStartSettings();
        setOriginalAndCurrentCodeConfiguration();
    }

    public String processCharacter(String character) {
        String output = engine.processCharacter(character);
        setOriginalAndCurrentCodeConfiguration();
        return output;
    }

    public boolean isValidInput(String character) {
        return engine.isStringContainOnlyLettersFromAlphabet(character.toUpperCase());
    }

    public void updateHistory(InputProcess newProcess) {
        engine.updateHistory(newProcess);
        setSpecification();
    }

    public boolean isDictionaryContainWordsFromInput(String text) {
        return engine.isDictionaryContainWordsFromInput(text);
    }

    public String excludeInputFromInvalidCharacters(String text) {
        return engine.removeExcludeChars(text);
    }

    public String processInputToBruteForce(String text) {
        String output = engine.decryptInput(text);
        setOriginalAndCurrentCodeConfiguration();
        return output;
    }

    public void startRunningBruteForce(BruteForceSettings bruteForceSettings) {
        cleanOldResultsOfBruteForce();
        UIAdapter uiAdapter = createUIAdapter();
        currentRunningTask = new BruteForceTask(engine.getDeepCopyOfEngine(), bruteForceSettings, uiAdapter,totalCompletedMissionsConsumer,avgTimePerMissionConsumer,totalTimeConsumer);
        bindTaskToUIComponents(currentRunningTask);
        new Thread(currentRunningTask).start();
    }

    public void stopBruteForce() {
        currentRunningTask.setPause(false);
        currentRunningTask.setResume(true);
        currentRunningTask.setCancelled(true);
    }

    public void pauseBruteForce() {
        currentRunningTask.setResume(false);
        currentRunningTask.setPause(true);
    }
    public void ResumeBruteForce() {
        currentRunningTask.setPause(false);
        currentRunningTask.setResume(true);
    }

    public void cleanOldResultsOfBruteForce() {
        totalMissions.set(0);
        totalCompletedMissions.set(0);
        taskProgress.set(0);
        taskMsg.set("");
        taskPercentage.set("");
        totalTime.set(0);
        avgTimePerMission.set(0);
        bodyComponentController.cleanOldResultsOfBruteForce();
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                new Consumer<AgentResponse>() {
                    @Override
                    public void accept(AgentResponse agentResponse) {
                        FullAppController.this.createTile(agentResponse);
                    }
                },
                totalMissions::set
        );
    }

    public void onTaskFinished() {
        this.taskMsg.unbind();
        this.taskProgress.unbind();
        this.taskPercentage.unbind();
        toggleStartStopOnFinish();
    }

    private void toggleStartStopOnFinish(){
        bodyComponentController.toggleStartStopOnFinish();
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask) {
        // task message
        taskMsg.bind(aTask.messageProperty());

        // task progress bar
        taskProgress.bind(aTask.progressProperty());

        // task percent label
        taskPercentage.bind(
                Bindings.concat(
                        Bindings.format("%.0f", Bindings.multiply(aTask.progressProperty(), 100)), " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue() == false) {
                onTaskFinished();
            }
        });
    }

    Consumer<Long> totalCompletedMissionsConsumer = value -> {
        Platform.runLater(
                () -> {
                    totalCompletedMissions.set(value);
                }
        );
    };

    Consumer<Long> avgTimePerMissionConsumer = value -> {
        Platform.runLater(
                () -> {
                    avgTimePerMission.set(value);
                }
        );
    };

    Consumer<Long> totalTimeConsumer = value -> {
        Platform.runLater(
                () -> {
                    totalTime.set(value);
                }
        );
    };

    private void createTile(AgentResponse agentResponse) {
        bodyComponentController.createTile(agentResponse);
    }

    public SimpleLongProperty totalMissionsProperty() {
        return totalMissions;
    }

    public SimpleLongProperty totalCompletedMissionsProperty() {
        return totalCompletedMissions;
    }

    public SimpleLongProperty totalTimeProperty() {
        return totalTime;
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

    public int getRotorCount() {
        return engine.getMachineRotorCount();
    }

    public void changeSkin(String value) {
        if(value.equals("Default")){
           fullAppScrollPane.getStylesheets().clear();
           fullAppScrollPane.getStylesheets().add("fullApp/darkMode.css");
        }
        if (value.equals("Style-1")){
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("fullApp/lightMode.css");
        }
        if (value.equals("Style-2")){
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("fullApp/freeStyle.css");
        }
    }

    public void rotateTransitionAnimation() {
        headerComponentController.rotateTransitionAnimation();
    }
}
