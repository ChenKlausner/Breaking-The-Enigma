package agent;

import decryptionManager.DecryptionManagerInfo;
import dto.AgentResponse;
import dto.CandidateToDecrypt;
import dto.CodeConfiguration;
import engine.Engine;
import utility.RotorsPositionsGenerator;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.stream.IntStream;

public class AgentTask implements Runnable {
    private Engine engine;
    private RotorsPositionsGenerator rotorsPositionsGenerator;
    private Integer taskSize;
    private BlockingDeque<AgentResponse> agentResponse;
    private List<CandidateToDecrypt> candidates;
    private DecryptionManagerInfo decryptionManagerInfo;
    private Integer[] totalCompletedMissions;
    private Integer[] totalCandidates;


    public AgentTask(Engine engine, RotorsPositionsGenerator rotorsPositionsGenerator, Integer taskSize, DecryptionManagerInfo decryptionManagerInfo) {
        this.engine = engine;
        this.rotorsPositionsGenerator = rotorsPositionsGenerator;
        this.taskSize = taskSize;
        this.candidates = new ArrayList<>();
        this.decryptionManagerInfo = decryptionManagerInfo;
    }

    @Override
    public void run() {
        rotorsPositionsGenerator.setStartPosition(rotorsPositionsGenerator.getRotorsPositions());
        IntStream.range(0, taskSize).forEachOrdered(i -> {
            rotorsPositionsGenerator.advance(i);
            engine.setRotorsPositions(rotorsPositionsGenerator.getRotorsPositions());
            engine.setMachineToStartSettings();
            CodeConfiguration currentCode = engine.getCurrentCodeConfig();
            String outputMsg = engine.decryptInput(decryptionManagerInfo.getSecretMessage());
            if (isCandidateToDecrypt(outputMsg.toLowerCase())) {
                candidates.add(new CandidateToDecrypt(outputMsg, currentCode, Thread.currentThread().getName()));
            }
            rotorsPositionsGenerator.initializeToStartPosition();
        });

        if (!candidates.isEmpty()) {
            totalCandidates[0] = totalCandidates[0] + candidates.size();
            try {
                agentResponse.put(new AgentResponse(candidates, false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        totalCompletedMissions[0]++;
    }

    private boolean isCandidateToDecrypt(String outputMsg) {
        String parts[] = outputMsg.split(" ");
        return Arrays.stream(parts).allMatch(word -> engine.getDictionary().contains(word));
    }

    public void setAgentResponse(BlockingDeque<AgentResponse> agentResponse) {
        this.agentResponse = agentResponse;
    }

    public void setTotalCompletedMissions(Integer[] totalCompletedMissions) {
        this.totalCompletedMissions = totalCompletedMissions;
    }

    public void setTotalCandidates(Integer[] totalCandidates) {
        this.totalCandidates = totalCandidates;
    }
}
