package subComponent.singleCandidate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SingleCandidateController {
    @FXML
    private Label candidateStringLabel;

    @FXML
    private Label threadNameLabel;

    @FXML
    private Label codeConfigurationLabel;

    @FXML
    private VBox tileVbox;

    public void setCandidateStringLabel(String candidateStringLabel) {
        this.candidateStringLabel.setText(candidateStringLabel);
    }

    public void setThreadNameLabel(String threadNameLabel) {
        this.threadNameLabel.setText(threadNameLabel);
    }

    public void setCodeConfigurationLabel(String codeConfigurationLabel) {
        this.codeConfigurationLabel.setText(codeConfigurationLabel);
    }

    public void setTileVbox(VBox tileVbox) {
        this.tileVbox = tileVbox;
    }
}
