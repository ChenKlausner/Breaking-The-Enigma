package uboatApp;

import com.google.gson.reflect.TypeToken;
import dto.*;
import engine.EngineUtility;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import subComponent.body.BodyController;
import subComponent.header.HeaderController;
import subComponent.login.LoginController;
import util.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.Constants.*;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class FullAppController {
    @FXML
    private GridPane headerComponent;
    @FXML
    private HeaderController headerComponentController;

    private TabPane loginComponent;
    private LoginController loginController;
    @FXML
    private AnchorPane bodyAnchorPane;

    private TabPane bodyComponent;
    private BodyController bodyComponentController;
    @FXML
    private BorderPane fullAppBoarderPane;
    @FXML
    private ScrollPane fullAppScrollPane;

    SingleSelectionModel<Tab> selectionModel;
    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private SimpleIntegerProperty possibleAmountOfRotors;
    private SimpleIntegerProperty amountOfRotorsInUse;
    private SimpleIntegerProperty amountOfReflectors;
    private SimpleIntegerProperty amountOfMassages;
    private SimpleStringProperty originalCodeConfiguration;
    private SimpleStringProperty currentCodeConfiguration;
    private SimpleBooleanProperty isCodeConfigurationSelected;
    private SimpleStringProperty userName;

    @FXML
    public void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }

        loadLoginPage();
        loadBodyComponent();

        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        possibleAmountOfRotors = new SimpleIntegerProperty(0);
        amountOfRotorsInUse = new SimpleIntegerProperty(0);
        amountOfReflectors = new SimpleIntegerProperty(0);
        amountOfMassages = new SimpleIntegerProperty(0);
        originalCodeConfiguration = new SimpleStringProperty();
        currentCodeConfiguration = new SimpleStringProperty();
        isCodeConfigurationSelected = new SimpleBooleanProperty(false);
        userName = new SimpleStringProperty();
        headerComponentController.bindHeaderToFullApp();
    }

    private void loadBodyComponent() {
        URL loginPageUrl = getClass().getResource(BODY_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            bodyComponent = fxmlLoader.load();
            bodyComponentController = fxmlLoader.getController();
            bodyComponentController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setMainController(this);
            setBodyAnchorPaneTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBodyAnchorPaneTo(Parent pane) {
        bodyAnchorPane.getChildren().clear();
        bodyAnchorPane.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToBody() {
        setBodyAnchorPaneTo(bodyComponent);
        bodyComponentController.bindBodyToFullApp();
        selectionModel = bodyComponent.getSelectionModel();
        //chatRoomComponentController.setActive();
    }

    public void readMachineData(String absolutePath) {
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);
        isCodeConfigurationSelected.set(false);
        setSpecification();
        initDictionaryInBruteForceTab(getDictionary());
        clearLastMachine();
        bodyComponentController.viewCodeConfigurationSetup(getMachineDetails().getRotorCount(), getReflectorIdList());
        bodyComponentController.startAllieTeamsDataRefresher();
    }

    private MachineDetailsDto getMachineDetails() {
        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_DETAILS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String json = null;
        MachineDetailsDto machineDetailsDto = null;
        try {
            Response response = call.execute();
            json = response.body().string();
            machineDetailsDto = GSON_INSTANCE.fromJson(json, MachineDetailsDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return machineDetailsDto;
    }

    private Set<String> getDictionary() {
        String finalUrl = HttpUrl
                .parse(Constants.DICTIONARY)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String jsonDict = null;
        Set<String> dictionary = null;
        try {
            Response response = call.execute();
            jsonDict = response.body().string();
            dictionary = GSON_INSTANCE.fromJson(jsonDict, new TypeToken<Set<String>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
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
            setMachineSettingsManually(codeConfiguration);
            setOriginalAndCurrentCodeConfiguration();
            isCodeConfigurationSelected.set(true);
        }
    }

    private void setMachineSettingsManually(CodeConfiguration codeConfiguration) {
        String jsonConfiguration = GSON_INSTANCE.toJson(codeConfiguration);

        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_MANUAL_CONFIGURATION)
                .newBuilder()
                .addQueryParameter("codeConfiguration", jsonConfiguration)
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

    public void setCodeConfigurationAutomatically() {
        setMachineSettingsAutomatically();
        setOriginalAndCurrentCodeConfiguration();
        isCodeConfigurationSelected.set(true);
    }

    private void setMachineSettingsAutomatically() {
        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_AUTO_CONFIGURATION)
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

    private void setSpecification() {
        Specification sp = getMachineSpecification();
        this.possibleAmountOfRotors.set(sp.getPossibleAmountOfRotors());
        this.amountOfRotorsInUse.set(sp.getAmountOfRotorsInUse());
        this.amountOfReflectors.set(sp.getAmountOfReflectors());
        this.amountOfMassages.set(sp.getAmountOfMassages());
    }

    private Specification getMachineSpecification() {
        String finalUrl = HttpUrl
                .parse(Constants.SPECIFICATION)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String jsonSpecification = null;
        Specification sp = null;
        try {
            Response response = call.execute();
            jsonSpecification = response.body().string();
            sp = GSON_INSTANCE.fromJson(jsonSpecification, Specification.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sp;
    }

    private void setOriginalAndCurrentCodeConfiguration() {
        originalCodeConfiguration.set(getDescriptionOfCurrentSettings(getCodeConfiguration(Constants.ORIGINAL_CODE_CONFIG)));
        currentCodeConfiguration.set(getDescriptionOfCurrentSettings(getCodeConfiguration(Constants.CURRENT_CODE_CONFIG)));
        bodyComponentController.setCurrentCodeComponent();
    }

    private CodeConfiguration getCodeConfiguration(String codeType) {
        String finalUrl = HttpUrl
                .parse(codeType)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String json = null;
        CodeConfiguration codeConfiguration = null;
        try {
            Response response = call.execute();
            json = response.body().string();
            codeConfiguration = GSON_INSTANCE.fromJson(json, CodeConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codeConfiguration;
    }

    public List<String> getReflectorIdList() {
        return IntStream.range(0, getMachineDetails().getReflectorsAmount()).mapToObj(i -> getReflectorId(i)).collect(Collectors.toList());
    }

    private String getReflectorId(int index) {
        String finalUrl = HttpUrl
                .parse(Constants.REFLECTOR_ID)
                .newBuilder()
                .addQueryParameter("index", String.valueOf(index))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String reflectorId = null;
        try {
            Response response = call.execute();
            reflectorId = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reflectorId;
    }

    public String getAlphabet() {
        return getMachineDetails().getAlphabet();
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

    private void clearLastMachine() {
        originalCodeConfiguration.set("");
        currentCodeConfiguration.set("");
        bodyComponentController.clearLastMachine();
    }

    public CodeConfiguration getCurrentCodeConfiguration() {
        return getCodeConfiguration(CURRENT_CODE_CONFIG);
    }

    private void popErrorMsg(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File loading error");
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    public void initializeToOriginalCode() {
        setMachineToStartSettings();
        setOriginalAndCurrentCodeConfiguration();
    }

    private void setMachineToStartSettings() {
        String finalUrl = HttpUrl
                .parse(Constants.STARTUP_SETTINGS)
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

    public boolean isValidInput(String character) {
        String finalUrl = HttpUrl
                .parse(Constants.VALID_INPUT)
                .newBuilder()
                .addQueryParameter("character", character.toUpperCase())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String json = null;
        boolean result = false;
        try {
            Response response = call.execute();
            json = response.body().string();
            result = GSON_INSTANCE.fromJson(json, boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isDictionaryContainWordsFromInput(String text) {
        String finalUrl = HttpUrl
                .parse(Constants.WORD_DICTIONARY_CHECK)
                .newBuilder()
                .addQueryParameter("text", text)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String json = null;
        boolean result = false;
        try {
            Response response = call.execute();
            json = response.body().string();
            result = GSON_INSTANCE.fromJson(json, boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String excludeInputFromInvalidCharacters(String text) {
        String finalUrl = HttpUrl
                .parse(Constants.REMOVE_EXCLUDED_CHARACTERS)
                .newBuilder()
                .addQueryParameter("text", text)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String result = null;
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String processInputToBruteForce(String text) {
        String output = decryptInput(text);
        setOriginalAndCurrentCodeConfiguration();
        return output;
    }

    private String decryptInput(String text) {
        String finalUrl = HttpUrl
                .parse(Constants.DECRYPT_INPUT)
                .newBuilder()
                .addQueryParameter("text", text)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String result = null;
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
        return getMachineDetails().getRotorCount();
    }

    public String getUserName() {
        return userName.get();
    }

    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void changeSkin(String value) {
        if (value.equals("Default")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("uboatApp/cssStyles/darkMode.css");
        }
        if (value.equals("Style-1")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("uboatApp/cssStyles/lightMode.css");
        }
        if (value.equals("Style-2")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("uboatApp/cssStyles/freeStyle.css");
        }
    }

    public void rotateTransitionAnimation() {
        headerComponentController.rotateTransitionAnimation();
    }

    public void enableLoadFile() {
        headerComponentController.enableLoadFile();
    }

    public void switchToLogin() {
        Platform.runLater(() -> {
            selectedFileProperty.set("");
            isFileSelected.set(false);
            isCodeConfigurationSelected.set(false);
            setBodyAnchorPaneTo(loginComponent);
            loginController.initUserTextField();
            userName.set("");
        });
    }

    public void cleanAllData() {
        Platform.runLater(() -> {
            this.possibleAmountOfRotors.set(0);
            this.amountOfRotorsInUse.set(0);
            this.amountOfReflectors.set(0);
            this.amountOfMassages.set(0);
            this.currentCodeConfiguration.set("");
            this.originalCodeConfiguration.set("");
            selectionModel.select(0);
        });
    }
}
