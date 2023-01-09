package agent;

import dto.AgentResponse;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AgentManager {
    private BlockingDeque<Runnable> agentTaskBlockingDeque;
    private ThreadPoolExecutor executor;
    private BlockingDeque<AgentResponse> agentResponse;
    private Integer numOfThreads;
    private Integer totalMissions;
    private Integer[] totalCompletedMissions = {0};
    private Integer[] totalCandidates = {0};

    public AgentManager(Integer numOfThreads) {
        this.agentTaskBlockingDeque = new LinkedBlockingDeque<>();
        this.numOfThreads = numOfThreads;
        this.executor = new ThreadPoolExecutor(numOfThreads, numOfThreads, 2, TimeUnit.SECONDS, agentTaskBlockingDeque);
        this.agentResponse = new LinkedBlockingDeque<>();
        this.totalMissions = 0;
    }

    public void startExecuteTasks() {
        executor.prestartAllCoreThreads();
    }

    public void stopExecuteTasks() {
        executor.shutdownNow();
    }

    public void addAgentTask(Runnable agentTask) {
        try {
            agentTaskBlockingDeque.put(agentTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BlockingDeque<AgentResponse> getAgentResponse() {
        return agentResponse;
    }

    public void clearAgentTask() {
        agentTaskBlockingDeque.clear();
    }

    public void clearAgentResponse() {
        agentResponse.clear();
    }

    public void clearData() {
        totalMissions = 0;
        totalCompletedMissions[0] = 0;
        totalCandidates[0] = 0;
    }

    public synchronized void addOneToTotalMissionsCounter() {
        totalMissions++;
    }

    public boolean isEmptyAgentTaskBlockingDeque() {
        return agentTaskBlockingDeque.isEmpty();
    }

    public Integer[] getTotalCompletedMissions() {
        return totalCompletedMissions;
    }

    public Integer[] getTotalCandidates() {
        return totalCandidates;
    }

    public synchronized Integer getTotalMissions() {
        return totalMissions;
    }

    public int getTotalCompletedMissionCount() {
        return totalCompletedMissions[0];
    }

    public int getTotalCandidatesCount() {
        return totalCandidates[0];
    }

    public int getCurrentAmountInQueue() {
        return agentTaskBlockingDeque.size();
    }
}
