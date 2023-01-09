package battlefield;

import dto.AgentResponse;
import dto.UBoatResponse;
import enums.ContestStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Battlefield implements Serializable {
    private String difficultyLevel;
    private String battleName;
    private int numOfAllies;
    private String uBoatUserName;
    private ContestStatus contestStatus;
    private Set<String> signedAllies;
    private String inputMsg;
    private String secretMsg;
    private boolean isUBoatReady;
    private boolean readyToStartContest;
    private List<UBoatResponse> uBoatResponseList;
    private boolean foundWinner;
    private String winnerName;

    public Battlefield(String difficultyLevel, String battleName, int numOfAllies, String uBoatUserName) {
        this.difficultyLevel = difficultyLevel;
        this.battleName = battleName;
        this.numOfAllies = numOfAllies;
        this.uBoatUserName = uBoatUserName;
        this.contestStatus = ContestStatus.WAITING;
        this.signedAllies = new HashSet<>();
        this.isUBoatReady = false;
        this.readyToStartContest = false;
        this.uBoatResponseList = new ArrayList<>();
        this.foundWinner = false;
        this.winnerName ="";
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getBattleName() {
        return battleName;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public String getuBoatUserName() {
        return uBoatUserName;
    }

    public String getListedTeamsVsNeededTeams() {
        return signedAllies.size() + "\\" + numOfAllies;
    }

    public String getContestStatus() {
        return contestStatus.toString();
    }

    public synchronized boolean addAllieTeam(String allieUserName) {
        if (signedAllies.size() < numOfAllies) {
            signedAllies.add(allieUserName);
            return true;
        }
        return false;
    }

    public synchronized void  removeAllieTeam(String allieUserName){
        signedAllies.remove(allieUserName);
    }

    public Set<String> getSignedAllies() {
        return signedAllies;
    }

    public void setInputMsg(String inputMsg) {
        this.inputMsg = inputMsg;
    }

    public void setSecretMsg(String secretMsg) {
        this.secretMsg = secretMsg;
    }

    public void setUBoatReady(boolean UBoatReady) {
        isUBoatReady = UBoatReady;
    }

    public void setReadyToStartContest(boolean readyToStartContest) {
        this.readyToStartContest = readyToStartContest;
    }

    public boolean isUBoatReady() {
        return isUBoatReady;
    }

    public boolean isReadyToStartContest() {
        return readyToStartContest;
    }
    public String getInputMsg() {
        return inputMsg;
    }

    public String getSecretMsg() {
        return secretMsg;
    }

    public void setContestStatus(ContestStatus contestStatus) {
        this.contestStatus = contestStatus;
    }

    synchronized public void addUBoatResponse(UBoatResponse uBoatResponse){
        uBoatResponseList.add(uBoatResponse);
    }

    synchronized public UBoatResponse takeUBoaResponse(){
        return uBoatResponseList.remove(0);
    }

    synchronized public boolean isEmptyUBoatResponseList(){
        return uBoatResponseList.isEmpty();
    }

    public boolean isFoundWinner() {
        return foundWinner;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public synchronized void setFoundWinner(boolean foundWinner) {
        this.foundWinner = foundWinner;
    }

    public synchronized void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public void clearResponseList(){
        uBoatResponseList.clear();
    }
}
