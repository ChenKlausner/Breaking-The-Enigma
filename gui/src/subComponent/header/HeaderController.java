package subComponent.header;

import javafx.animation.RotateTransition;
import javafx.animation.StrokeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import fullApp.FullAppController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

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
    void loadXmlButtonPressedHandler(ActionEvent event) {
        FileChooser fileChooserBtn = new FileChooser();
        fileChooserBtn.setTitle("XML File Chooser");
        fileChooserBtn.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooserBtn.showOpenDialog(null);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        mainController.readMachineData(absolutePath);
    }

    public void setMainController(FullAppController mainController) {
        this.mainController = mainController;
    }

    public void bindHeaderToFullApp(){
        xmlPathTextField.textProperty().bind(mainController.selectedFilePropertyProperty());
        skinCombobox.getItems().addAll("Default", "Style-1", "Style-2");
        skinCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                mainController.changeSkin(skinCombobox.getValue());
            }
        });
    }

    public void rotateTransitionAnimation() {
        RotateTransition rt = new RotateTransition(Duration.millis(700), enigmaImj);
        rt.setByAngle(180);
        rt.setCycleCount(4);
        rt.setAutoReverse(true);

        rt.play();
    }
}

