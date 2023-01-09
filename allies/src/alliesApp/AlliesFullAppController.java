package alliesApp;


import dto.AgentResponse;
import dto.CodeConfiguration;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import subComponent.body.BodyController;
import subComponent.header.HeaderController;
import subComponent.login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static util.Constants.*;

public class AlliesFullAppController {

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

    SingleSelectionModel<Tab> selectionModel;
    @FXML
    private BorderPane fullAppBoarderPane;
    @FXML
    private ScrollPane fullAppScrollPane;

    private String uBoatUserNameThatCurrentAliieSignTo;

    private SimpleStringProperty userName;


    @FXML
    public void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }

        loadLoginPage();
        loadBodyComponent();

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
        selectionModel = bodyComponent.getSelectionModel();
        //bodyComponentController.bindBodyToFullApp();
        //chatRoomComponentController.setActive();
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

    public void changeSkin(String value) {
        if (value.equals("Default")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("alliesApp/cssStyles/darkMode.css");
        }
        if (value.equals("Style-1")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("alliesApp/cssStyles/lightMode.css");
        }
        if (value.equals("Style-2")) {
            fullAppScrollPane.getStylesheets().clear();
            fullAppScrollPane.getStylesheets().add("alliesApp/cssStyles/freeStyle.css");
        }
    }

    public void startDashboardRefreshers() {
        bodyComponentController.startDashboardRefresher();
    }

    public void setUboatName(String uBoatUserName) {
        uBoatUserNameThatCurrentAliieSignTo = uBoatUserName;
    }

    public String getUboatName() {
        return uBoatUserNameThatCurrentAliieSignTo;
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

    public void switchTabs() {
        selectionModel.select(0);
    }

    public void switchTabToContest() {
        selectionModel.select(1);
    }
}
