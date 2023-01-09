package tasks;

import dto.AgentResponse;
import decryptionManager.BruteForceProcessInfo;
import decryptionManager.DecryptionManager;
import decryptionManager.DifficultyLevel;
import engine.Engine;
import fullApp.UIAdapter;
import javafx.concurrent.Task;
import subComponent.bruteForceTab.BruteForceSettings;

import java.util.function.Consumer;

public class BruteForceTask extends Task<Boolean> {
    private Engine engineCopy;
    private String secretMessage;
    private Integer taskSize;
    private Integer numOfAgents;
    private DifficultyLevel difficultyLevel;
    private UIAdapter uiAdapter;
    private boolean isCancelled;
    private boolean isPause;
    private boolean isResume;
    private DecryptionManager decryptionManager;
    private Consumer<Long> totalCompletedMissionsConsumer;
    private Consumer<Long> avgTimePerMissionConsumer;
    private Consumer<Long> totalTimeConsumer;

    public BruteForceTask(Engine engineCopy, BruteForceSettings bruteForceSettings, UIAdapter uiAdapter, Consumer<Long> totalCompletedMissionsConsumer, Consumer<Long> avgTimePerMissionConsumer, Consumer<Long> totalTimeConsumer) {
        this.engineCopy = engineCopy;
        this.secretMessage = bruteForceSettings.getSecretMsg();
        this.taskSize = bruteForceSettings.getTaskSize();
        this.numOfAgents = bruteForceSettings.getAgentsNumber();
        this.difficultyLevel = bruteForceSettings.getDifficultyLevel();
        this.uiAdapter = uiAdapter;
        this.totalCompletedMissionsConsumer = totalCompletedMissionsConsumer;
        this.avgTimePerMissionConsumer = avgTimePerMissionConsumer;
        this.totalTimeConsumer = totalTimeConsumer;
        this.isCancelled = false;
        this.isPause = false;
        this.isResume = false;
    }

    @Override
    protected Boolean call() throws Exception {

       /* updateValue(true);
        decryptionManager = new DecryptionManager(engineCopy, secretMessage, taskSize, difficultyLevel, numOfAgents, totalCompletedMissionsConsumer, avgTimePerMissionConsumer, totalTimeConsumer);
        uiAdapter.updateTotalMissions(decryptionManager.getTotalMissions());

        Runnable generateAgentTasks = () -> {
            decryptionManager.start();
        };
        new Thread(generateAgentTasks, "pushAgentTasksThread").start();

        updateMessage("Brute force in progress....");
        updateProgress(0, decryptionManager.getTotalMissions());

        Runnable updateCandidates = () -> {
            AgentResponse agentResponse = null;
            do {
                if (isPause) {
                    decryptionManager.setPaused(true);
                    updateMessage("Paused..");
                }

                if (isCancelled) {
                    updateMessage("Cancelled!");
                    decryptionManager.setCancelled(true);
                    updateValue(false);
                    break;
                }
                try {
                    agentResponse = decryptionManager.getAgentResponse().take();
                    uiAdapter.addNewCandidates(agentResponse);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (!decryptionManager.isDone() && !agentResponse.isLastResponse());
        };
        new Thread(updateCandidates, "candidatesThread").start();

        Runnable updateProgressRunnable = () -> {
            BruteForceProcessInfo bruteForceProcessInfo = null;
            do {
                if (isResume) {
                    decryptionManager.setPaused(false);
                    updateMessage("Brute force in progress....");
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bruteForceProcessInfo = decryptionManager.getBruteForceProcessInfo();
                updateProgress(bruteForceProcessInfo.getCompletedMissions(), decryptionManager.getTotalMissions());
            } while (!decryptionManager.isDone());

            decryptionManager.addPoisonPill();
            updateMessage(isCancelled ? "Cancelled!" : "Done!");
            updateValue(false);
        };
        new Thread(updateProgressRunnable, "dataThread").start();
*/
        return Boolean.TRUE;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }
}