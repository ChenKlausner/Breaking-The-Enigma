package dto;

public class WinnerResponse {
    private Boolean winnerWasFound;
    private String WinnerName;

    public WinnerResponse(Boolean winnerWasFound, String winnerName) {
        this.winnerWasFound = winnerWasFound;
        WinnerName = winnerName;
    }

    public Boolean getWinnerWasFound() {
        return winnerWasFound;
    }

    public String getWinnerName() {
        return WinnerName;
    }
}
