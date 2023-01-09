package subComponent.login;

import agentApp.AgentFullAppController;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Set;

import static util.Constants.GSON_INSTANCE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class LoginController {
    @FXML
    public TextField userNameTextField;
    @FXML
    public Label errorMessageLabel;
    private AgentFullAppController mainController;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    @FXML
    private ComboBox<String> allieTeamsComboBox;
    @FXML
    private ComboBox<Integer> amountOfThreadComboBox;
    @FXML
    private TextField amountOfTasksTextField;


    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        /*HttpClientUtil.setCookieManagerLoggingFacility(line ->
                Platform.runLater(() ->
                        updateHttpStatusLine(line)));*/
        initAlliesTeamsComboBox();
        amountOfThreadComboBox.getItems().addAll(1, 2, 3, 4);
        amountOfThreadComboBox.getSelectionModel().selectFirst();
    }

    private void initAlliesTeamsComboBox() {
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_USERS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        String json = null;
        Set<String> alliesUserNames = null;
        try {
            Response response = call.execute();
            json = response.body().string();
            alliesUserNames = GSON_INSTANCE.fromJson(json, new TypeToken<Set<String>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        allieTeamsComboBox.getItems().addAll(FXCollections.observableArrayList(alliesUserNames));
        allieTeamsComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        if (amountOfTasksTextField.getText().isEmpty() || Integer.parseInt(amountOfTasksTextField.getText()) <= 0) {
            errorMessageProperty.set("Amount of Tasks should be positive number");
            return;
        }

        if (allieTeamsComboBox.getValue().isEmpty()){
            errorMessageProperty.set("Please wait to team allie to login");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.USER_LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("usertype", "Agent")
                .addQueryParameter("tasks", amountOfTasksTextField.getText())
                .addQueryParameter("numOfThreads", String.valueOf(amountOfThreadComboBox.getValue()))
                .addQueryParameter("allieTeamName", allieTeamsComboBox.getValue())
                .build()
                .toString();

        /*updateHttpStatusLine("New request is launched for: " + finalUrl);*/

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        mainController.switchToBody();
                        mainController.setAllieTeamName(allieTeamsComboBox.getValue());
                        mainController.setNumOfThreads(amountOfThreadComboBox.getValue());
                        mainController.setUserName(userName);
                        mainController.startAgentContestData();
                    });
                }
            }
        });
    }

    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

   /* private void updateHttpStatusLine(String data) {
        chatAppMainController.updateHttpLine(data);
    }*/

    public void setMainController(AgentFullAppController mainController) {
        this.mainController = mainController;
    }
}
