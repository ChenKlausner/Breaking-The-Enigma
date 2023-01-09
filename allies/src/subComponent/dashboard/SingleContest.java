package subComponent.dashboard;

import dto.BattlefieldDto;
import javafx.beans.property.SimpleStringProperty;

public class SingleContest {
    private SimpleStringProperty battleFieldName;
    private SimpleStringProperty uBoatUserName;
    private SimpleStringProperty status;
    private SimpleStringProperty difficultyLevel;
    private SimpleStringProperty listedTeamsVsNeededTeams;

    public SingleContest(String battleFieldName, String uBoatUserName, String status, String difficultyLevel, String listedTeamsVsNeededTeams) {
        this.battleFieldName = new SimpleStringProperty(battleFieldName);
        this.uBoatUserName = new SimpleStringProperty(uBoatUserName);
        this.status = new SimpleStringProperty(status);
        this.difficultyLevel = new SimpleStringProperty(difficultyLevel);
        this.listedTeamsVsNeededTeams = new SimpleStringProperty(listedTeamsVsNeededTeams);
    }

    public SingleContest(BattlefieldDto battlefieldDto) {
        this.battleFieldName = new SimpleStringProperty(battlefieldDto.getBattleName());
        this.uBoatUserName = new SimpleStringProperty(battlefieldDto.getuBoatUserName());
        this.status = new SimpleStringProperty(battlefieldDto.getContestStatus());
        this.difficultyLevel = new SimpleStringProperty(battlefieldDto.getDifficultyLevel());
        this.listedTeamsVsNeededTeams = new SimpleStringProperty(battlefieldDto.getListedTeamsVsNeededTeams());
    }

    public String getBattleFieldName() {
        return battleFieldName.get();
    }

    public SimpleStringProperty battleFieldNameProperty() {
        return battleFieldName;
    }

    public String getuBoatUserName() {
        return uBoatUserName.get();
    }

    public SimpleStringProperty uBoatUserNameProperty() {
        return uBoatUserName;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getDifficultyLevel() {
        return difficultyLevel.get();
    }

    public SimpleStringProperty difficultyLevelProperty() {
        return difficultyLevel;
    }

    public String getListedTeamsVsNeededTeams() {
        return listedTeamsVsNeededTeams.get();
    }

    public SimpleStringProperty listedTeamsVsNeededTeamsProperty() {
        return listedTeamsVsNeededTeams;
    }

    public void setBattleFieldName(String battleFieldName) {
        this.battleFieldName.set(battleFieldName);
    }

    public void setuBoatUserName(String uBoatUserName) {
        this.uBoatUserName.set(uBoatUserName);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel.set(difficultyLevel);
    }

    public void setListedTeamsVsNeededTeams(String listedTeamsVsNeededTeams) {
        this.listedTeamsVsNeededTeams.set(listedTeamsVsNeededTeams);
    }
}
