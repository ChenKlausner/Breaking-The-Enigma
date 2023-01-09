package decryptionManager;

public class BruteForceProcessInfo {
    private long completedMissions;
    private long totalTime;
    private boolean isDone;

    public BruteForceProcessInfo( long completedMissions, long totalTime, boolean isDone) {
        this.completedMissions = completedMissions;
        this.totalTime = totalTime;
        this.isDone = isDone;
    }

    public long getCompletedMissions() {
        return completedMissions;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public boolean isDone() {
        return isDone;
    }
}
