package dto;

import dto.CodeConfiguration;

import java.io.Serializable;

public class CandidateToDecrypt implements Serializable {
    private String decryptMsg;
    private CodeConfiguration codeConfiguration;
    private String threadId;

    public CandidateToDecrypt(String decryptMsg, CodeConfiguration codeConfiguration, String threadId) {
        this.decryptMsg = decryptMsg;
        this.codeConfiguration = codeConfiguration;
        this.threadId = threadId;
    }

    public CodeConfiguration getCodeConfiguration() {
        return codeConfiguration;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getDecryptMsg() {
        return decryptMsg;
    }
}
