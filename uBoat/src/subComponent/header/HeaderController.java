package subComponent.header;

import javafx.application.Platform;
import javafx.scene.control.*;
import okhttp3.*;
import uboatApp.FullAppController;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import util.Constants;

import java.io.File;
import java.io.IOException;

import static util.http.HttpClientUtil.HTTP_CLIENT;

public class HeaderController {

    private FullAppController mainController;
    @FXML
    private Label enigmaImgLabel;
    @FXML
    private ImageView enigmaImj;
    @FXML
    private Button loadXmlButton;
    @FXML
    private TextField xmlPathTextField;
    @FXML
    private ComboBox<String> skinCombobox;
    @FXML
    private Label userNameLabel;

    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String goodStyle = String.format("-fx-border-color: GREEN; -fx-border-width: 2; -fx-border-radius: 5;");

    @FXML
    void loadXmlButtonPressedHandler(ActionEvent event) {
        FileChooser fileChooserBtn = new FileChooser();
        fileChooserBtn.setTitle("XML File Chooser");
        fileChooserBtn.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooserBtn.showOpenDialog(null);
        if (selectedFile == null) {
            return;
        } else {
            RequestBody body =
                    new MultipartBody.Builder()
                            .addFormDataPart(
                                    "file",
                                    selectedFile.getName(),
                                    RequestBody.create(
                                            selectedFile,
                                            MediaType.parse("text/plain")))
                            .build();

            Request request = new Request.Builder()
                    .url(Constants.LOAD_XML)
                    .post(body)
                    .build();

            Call call = HTTP_CLIENT.newCall(request);

            try {
                Response response = call.execute();
                String msg = response.body().string();
                if (response.code() == 200) {
                    String absolutePath = selectedFile.getAbsolutePath();
                    mainController.readMachineData(absolutePath);
                    loadXmlButton.setDisable(true);
                    xmlPathTextField.setStyle(goodStyle);
                }else {
                    popErrorMsg(msg);
                    xmlPathTextField.setStyle(errorStyle);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMainController(FullAppController mainController) {
        this.mainController = mainController;
    }

    public void bindHeaderToFullApp() {
        xmlPathTextField.textProperty().bind(mainController.selectedFilePropertyProperty());
        skinCombobox.getItems().addAll("Default", "Style-1", "Style-2");
        skinCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                mainController.changeSkin(skinCombobox.getValue());
            }
        });
        userNameLabel.textProperty().bind(mainController.userNameProperty());
    }

    public void rotateTransitionAnimation() {
        RotateTransition rt = new RotateTransition(Duration.millis(700), enigmaImj);
        rt.setByAngle(180);
        rt.setCycleCount(4);
        rt.setAutoReverse(true);
        rt.play();
    }

    private void popErrorMsg(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File loading error");
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    public void enableLoadFile() {
        loadXmlButton.setDisable(false);
    }

}

