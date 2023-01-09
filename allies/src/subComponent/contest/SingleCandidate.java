package subComponent.contest;

import javafx.beans.property.SimpleStringProperty;

public class SingleCandidate {
    private SimpleStringProperty candidateAgentName;
    private SimpleStringProperty decryptMsg;
    private SimpleStringProperty codeConfiguration;

    public SingleCandidate(String candidateAgentName,String decryptMsg, String codeConfiguration) {
        this.candidateAgentName = new SimpleStringProperty(candidateAgentName);
        this.decryptMsg = new SimpleStringProperty(decryptMsg);
        this.codeConfiguration = new SimpleStringProperty(codeConfiguration);
    }

    public String getCandidateAgentName() {
        return candidateAgentName.get();
    }

    public SimpleStringProperty candidateAgentNameProperty() {
        return candidateAgentName;
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
