package subComponent.bruteForceTab;

import javafx.beans.property.SimpleStringProperty;

public class SingleCandidate {
    private SimpleStringProperty allieName;
    private SimpleStringProperty decryptMsg;
    private SimpleStringProperty codeConfiguration;

    public SingleCandidate(String allieName, String decryptMsg, String codeConfiguration) {
        this.allieName = new SimpleStringProperty(allieName);
        this.decryptMsg = new SimpleStringProperty(decryptMsg);
        this.codeConfiguration = new SimpleStringProperty(codeConfiguration);
    }

    public String getAllieName() {
        return allieName.get();
    }

    public SimpleStringProperty allieNameProperty() {
        return allieName;
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
