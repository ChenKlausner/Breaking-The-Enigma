package subComponent.dashboard;

import javafx.beans.property.SimpleStringProperty;

public class SingleCandidate {
    private SimpleStringProperty decryptMsg;
    private SimpleStringProperty codeConfiguration;

    public SingleCandidate(String decryptMsg, String codeConfiguration) {
        this.decryptMsg = new SimpleStringProperty(decryptMsg);
        this.codeConfiguration = new SimpleStringProperty(codeConfiguration);
    }

    public String getDecryptMsg() {
        return decryptMsg.get();
    }

    public SimpleStringProperty decryptMsgProperty() {
        return decryptMsg;
    }

    public String getCodeConfiguration() {
        return codeConfiguration.get();
    }

    public SimpleStringProperty codeConfigurationProperty() {
        return codeConfiguration;
    }
}
