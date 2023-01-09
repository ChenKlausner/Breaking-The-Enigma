package subComponent.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import alliesApp.AlliesFullAppController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField userNameTextField;
    @FXML
    public Label errorMessageLabel;
    private AlliesFullAppController mainController;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        /*HttpClientUtil.setCookieManagerLoggingFacility(line ->
                Platform.runLater(() ->
                        updateHttpStatusLine(line)));*/
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.USER_LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("usertype", "Allies")
                .build()
                .toString();

        /*updateHttpStatusLine("New request is launched for: " + finalUrl);*/

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    System.out.println(e.getMessage());
                });
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
                        mainController.startDashboardRefreshers();
                        mainController.setUserName(userName);
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

    public void setMainController(AlliesFullAppController mainController) {
        this.mainController = mainController;
    }
}
