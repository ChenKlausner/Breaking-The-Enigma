package decryptionManager;

import dto.AgentResponse;
import agent.AgentTask;
import component.Rotor;
import engine.Engine;
import utility.Permuter;
import utility.RotorsPositionsGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DecryptionManager {
    private Engine engineCopy;
    private RotorsPositionsGenerator rotorsPositionsGenerator;
    private DifficultyLevel difficultyLevel;
    private Integer taskSize;
    private long totalMissions;
    private DecryptionManagerInfo decryptionManagerInfo;
    private BlockingDeque<Runnable> agentTaskBlockingDeque;
    private boolean isDone;
    private boolean isCancelled;

    public DecryptionManager(Engine engineCopy, String secretMessage, Integer taskSize, DifficultyLevel difficultyLevel) {
        this.engineCopy = engineCopy;
        this.difficultyLevel = difficultyLevel;
        this.taskSize = taskSize;
        this.rotorsPositionsGenerator = new RotorsPositionsGenerator(engineCopy.getMachineAlphabet(), engineCopy.getMachineRotorCount());
        this.totalMissions = calcTotalMissions();
        this.decryptionManagerInfo = new DecryptionManagerInfo(secretMessage);
        this.agentTaskBlockingDeque = new LinkedBlockingDeque<>(1000);
        this.isDone = false;
        this.isCancelled = false;
    }

    public void start() {
        try {
            switch (difficultyLevel) {
                case EASY:
                    generateEasyAgentTasks();
                    break;
                case MEDIUM:
                    generateMediumAgentTasks();
                    break;
                case HARD:
                    generateHardAgentTasks();
                    break;
                case IMPOSSIBLE:
                    generateImpossibleAgentTasks();
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isDone = true;
    }

    private void generateImpossibleAgentTasks() throws InterruptedException {
        List<int[]> subset = generateSubsetCombinations(engineCopy.getMachineRotorList().size(), engineCopy.getMachineRotorCount());
        for (int[] set : subset) {
            List<Rotor> newSetOfRotorInUse = Arrays.stream(set).mapToObj(j -> engineCopy.getMachineRotorList().get(j)).collect(Collectors.toList());
            engineCopy.setRotorInUse(newSetOfRotorInUse);
            generateHardAgentTasks();
        }
    }

    private void generateHardAgentTasks() throws InterruptedException {
        Permuter permuter = new Permuter(engineCopy.getMachineRotorCount());
        int[] permutation = permuter.getNext();
        while (permutation != null) {
            List<Rotor> newPerOfRotorInUse = new ArrayList<>();
            for (int i = 0; i < engineCopy.getMachineRotorCount(); i++) {
                newPerOfRotorInUse.add(i, engineCopy.getMachineRotorInUseList().get(permutation[i]));
            }
            engineCopy.setRotorInUse(newPerOfRotorInUse);
            generateMediumAgentTasks();
            permutation = permuter.getNext();
        }
    }

    private void generateMediumAgentTasks() throws InterruptedException {
        for (int i = 0; i < engineCopy.getMachineReflectorList().size(); i++) {
            engineCopy.setReflectorToMachine(engineCopy.getMachineReflectorList().get(i));
            generateEasyAgentTasks();
        }
    }

    private void generateEasyAgentTasks() throws InterruptedException {
        rotorsPositionsGenerator.initRotorsPositionsToZeroPosition();
        engineCopy.setRotorsPositions(rotorsPositionsGenerator.getRotorsPositions());
        engineCopy.setMachineToStartSettings();
        long numOfRotorsCodeConfigurationOptions = getNumOfTotalOptionalCodeConfigurationToCheck(DifficultyLevel.EASY);
        long leftovers = numOfRotorsCodeConfigurationOptions % taskSize;
        //executor.prestartAllCoreThreads();
        for (int i = 0; i < numOfRotorsCodeConfigurationOptions - leftovers; i += taskSize) {
            if (isCancelled) {
                doWhenBruteForceIsCancelled();
                return;
            }
            agentTaskBlockingDeque.put(new AgentTask(engineCopy.getDeepCopyOfEngine(), rotorsPositionsGenerator.getDeepCopy(), taskSize, decryptionManagerInfo));
            rotorsPositionsGenerator.advance(taskSize);
            engineCopy.setRotorsPositions(rotorsPositionsGenerator.getRotorsPositions());
            engineCopy.setMachineToStartSettings();
        }
        if (leftovers != 0) {
            agentTaskBlockingDeque.put(new AgentTask(engineCopy.getDeepCopyOfEngine(), rotorsPositionsGenerator.getDeepCopy(), (int) leftovers, decryptionManagerInfo));
        }
    }

    public long getNumOfTotalOptionalCodeConfigurationToCheck(DifficultyLevel difficultyLevel) {
        int alphabetLength = engineCopy.getMachineAlphabet().length();
        int rotorCount = engineCopy.getMachineRotorCount();
        int numOfReflectors = engineCopy.getMachineReflectorList().size();
        int totalNumOfRotors = engineCopy.getMachineRotorList().size();
        long numOfTotalOptionalCodeConfiguration = 0;
        switch (difficultyLevel) {
            case EASY:
                numOfTotalOptionalCodeConfiguration = (long) Math.pow(alphabetLength, rotorCount);
                break;
            case MEDIUM:
                numOfTotalOptionalCodeConfiguration = (long) Math.pow(alphabetLength, rotorCount) * numOfReflectors;
                break;
            case HARD:
                numOfTotalOptionalCodeConfiguration = (long) Math.pow(alphabetLength, rotorCount) * numOfReflectors * factorial(rotorCount);
                break;
            case IMPOSSIBLE:
                numOfTotalOptionalCodeConfiguration = (long) Math.pow(alphabetLength, rotorCount) * numOfReflectors * factorial(rotorCount) * binomial(totalNumOfRotors, rotorCount);
                break;
        }
        return numOfTotalOptionalCodeConfiguration;
    }

    private long calcTotalMissions() {
        float numOfTotalCodeToGoOver = getNumOfTotalOptionalCodeConfigurationToCheck(this.difficultyLevel);
        float numOfEasyLevelCodes = getNumOfTotalOptionalCodeConfigurationToCheck(DifficultyLevel.EASY);
        float extraMissionsInEdges = numOfTotalCodeToGoOver / numOfEasyLevelCodes;
        float totalMissions = 0;
        if (numOfEasyLevelCodes % taskSize != 0) {
            totalMissions += extraMissionsInEdges;
        }
        totalMissions += (numOfTotalCodeToGoOver - (extraMissionsInEdges * (numOfEasyLevelCodes % taskSize))) / taskSize;
        return (long) totalMissions;
    }

    private long factorial(int rotorCount) {
        return LongStream.rangeClosed(1, rotorCount)
                .reduce(1, (long x, long y) -> x * y);
    }

    private int binomial(int n, int k) {
        // Base Cases
        if (k > n)
            return 0;
        if (k == 0 || k == n)
            return 1;
        // Recur
        return binomial(n - 1, k - 1) + binomial(n - 1, k);
    }

    public List<int[]> generateSubsetCombinations(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }
        return combinations;
    }

    public long getTotalMissions() {
        return totalMissions;
    }

    /*public BlockingDeque<AgentResponse> getAgentResponse() {
        return agentResponse;
    }*/

    public boolean isDone() {
        return isDone;
    }

    public BruteForceProcessInfo getBruteForceProcessInfo() {
        return new BruteForceProcessInfo(decryptionManagerInfo.getCompletedMissions(), 0, isDone);
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    private void doWhenBruteForceIsCancelled() {
        //executor.shutdownNow();
        //agentResponse.clear();
        agentTaskBlockingDeque.clear();
        isDone = true;
    }


    public void addPoisonPill() {
       /* try {
            agentResponse.put(new AgentResponse(new ArrayList<>(), true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public boolean isEmptyAgentTaskBlockingDeque(){
        return agentTaskBlockingDeque.isEmpty();
    }

    public BlockingDeque<Runnable> getAgentTaskBlockingDeque() {
        return agentTaskBlockingDeque;
    }
}
