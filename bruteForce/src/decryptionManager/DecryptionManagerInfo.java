package decryptionManager;

public class DecryptionManagerInfo {
    private String secretMessage;
    private long completedMissions;
    private long totalTime;

    public DecryptionManagerInfo(String secretMessage) {
        this.secretMessage = secretMessage;
        this.completedMissions = 0;
        this.totalTime = 0;
    }
    public String getSecretMessage() {
        return secretMessage;
    }
    public synchronized long getCompletedMissions() {
        return completedMissions;
    }

    public long getTotalTime() {
        return totalTime;
    }
    public synchronized void increaseCompletedMissions() {
        this.completedMissions++;
    }

    public synchronized void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
