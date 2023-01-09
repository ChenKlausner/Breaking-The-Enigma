package subComponent.header;

import agentApp.AgentFullAppController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class HeaderController {

    private AgentFullAppController mainController;
    @FXML
    private Label enigmaImgLabel;
    @FXML
    private ImageView enigmaImj;
    @FXML
    private ComboBox<String> skinCombobox;
    @FXML
    private Label userNameLabel;


    public void setMainController(AgentFullAppController mainController) {
        this.mainController = mainController;
    }

    public void bindHeaderToFullApp() {
        skinCombobox.getItems().addAll("Default", "Style-1", "Style-2");
        skinCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                mainController.changeSkin(skinCombobox.getValue());
            }
        });
        userNameLabel.textProperty().bind(mainController.userNameProperty());
    }
}

