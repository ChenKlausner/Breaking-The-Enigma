package agentApp;

import com.google.gson.reflect.TypeToken;
import dto.AgentResponse;
import dto.CodeConfiguration;
import dto.MachineDetailsDto;
import dto.Specification;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
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

public class AgentFullAppController {

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
    private SimpleStringProperty userName;
    private SimpleStringProperty allieTeamName;
    private SimpleIntegerProperty numOfThreads;


    @FXML
    public void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }

        loadLoginPage();
        loadBodyComponent();
        userName = new SimpleStringProperty();
        allieTeamName = new SimpleStringProperty();
        numOfThreads = new SimpleIntegerProperty();
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
        bodyComponentController.startToCheckIfNeedToWork();
        bodyComponentController.bindBodyToFullApp();
        //chatRoomComponentController.setActive();
    }

    public void changeSkin(String value) {
        if (value.equals("Default")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("agentApp/cssStyles/darkMode.css");
        }
        if (value.equals("Style-1")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("agentApp/cssStyles/lightMode.css");
        }
        if (value.equals("Style-2")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("agentApp/cssStyles/freeStyle.css");
        }
    }


    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public SimpleStringProperty allieTeamNameProperty() {
        return allieTeamName;
    }

    public void setAllieTeamName(String allieTeamName) {
        this.allieTeamName.set(allieTeamName);
    }

    public void startAgentContestData() {
        bodyComponentController.startAgentContestData();
    }


    public SimpleIntegerProperty numOfThreadsProperty() {
        return numOfThreads;
    }

    public void setNumOfThreads(int numOfThreads) {
        this.numOfThreads.set(numOfThreads);
    }
}
