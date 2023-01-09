package dto;

public class BattlefieldDto {
    private String battleName;
    private String uBoatUserName;
    private String  contestStatus;
    private String difficultyLevel;
    private String listedTeamsVsNeededTeams;

    public BattlefieldDto(String battleName, String uBoatUserName, String contestStatus, String difficultyLevel, String listedTeamsVsNeededTeams) {
        this.battleName = battleName;
        this.uBoatUserName = uBoatUserName;
        this.contestStatus = contestStatus;
        this.difficultyLevel = difficultyLevel;
        this.listedTeamsVsNeededTeams = listedTeamsVsNeededTeams;
    }

    public String getBattleName() {
        return battleName;
    }

    public String getuBoatUserName() {
        return uBoatUserName;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getListedTeamsVsNeededTeams() {
        return listedTeamsVsNeededTeams;
    }
}
