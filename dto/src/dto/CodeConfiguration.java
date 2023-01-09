package dto;

import java.io.Serializable;
import java.util.*;

public class CodeConfiguration implements Serializable {
    private LinkedHashMap<Integer, Character> rotorsInUse;
    private Map<Integer, Integer> notchDistanceFromWindow;
    private String reflectorId;
    private Map<Character, Character> plugsInUse;

    public CodeConfiguration(LinkedHashMap<Integer, Character> rotorsInUse, Map<Integer, Integer> notchDistanceFromWindow, String reflectorId, Map<Character, Character> plugsInUse) {
        this.rotorsInUse = rotorsInUse;
        this.notchDistanceFromWindow = notchDistanceFromWindow;
        this.reflectorId = reflectorId;
        this.plugsInUse = plugsInUse;
    }

    public LinkedHashMap<Integer, Character> getRotorsInUse() {
        return rotorsInUse;
    }

    public String getReflectorId() {
        return reflectorId;
    }

    public Map<Character, Character> getPlugsInUse() {
        return plugsInUse;
    }

    public Map<Integer, Integer> getNotchDistanceFromWindow() {
        return notchDistanceFromWindow;
    }
}
