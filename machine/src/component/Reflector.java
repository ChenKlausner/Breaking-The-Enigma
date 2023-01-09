package component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflector implements Comparable<Reflector> , Serializable {
    private final String id;
    private Map<Integer, Integer> connectedPairs;

    public Reflector(String id) {
        this.id = id;
        this.connectedPairs = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public int reflect(int currentEntry) {
        return connectedPairs.get(currentEntry);
    }

    public void addPair(int input, int output) {
        connectedPairs.put(input, output);
        connectedPairs.put(output, input);
    }

    @Override
    public int compareTo(Reflector o) {
        return id.compareTo(o.id);
    }
}
