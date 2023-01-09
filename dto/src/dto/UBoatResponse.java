package dto;

import java.io.Serializable;
import java.util.List;

public class UBoatResponse implements Serializable {
    private List<CandidateToDecrypt> candidates;
    private String allieUserName;
    private boolean isWinner;


    public UBoatResponse(List<CandidateToDecrypt> candidates, String allieUserName) {
        this.candidates = candidates;
        this.allieUserName = allieUserName;
        this.isWinner = false;
    }

    public List<CandidateToDecrypt> getCandidates() {
        return candidates;
    }

    public String getAllieUserName() {
        return allieUserName;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }
}
