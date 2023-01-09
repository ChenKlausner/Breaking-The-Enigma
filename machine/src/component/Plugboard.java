package component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Plugboard implements Serializable {
    private Map<Character, Character> connectedPlugs;

    public Plugboard() {
        connectedPlugs = new HashMap<>();
    }

    public Map<Character, Character> getConnectedPlugs() {
        return connectedPlugs;
    }

    public char swapCharacters(char inputChar){
        return connectedPlugs.containsKey(inputChar) ? connectedPlugs.get(inputChar) : Character.valueOf(inputChar);
    }

    public void addPair(char a, char b) {
        connectedPlugs.put(a, b);
        connectedPlugs.put(b, a);
    }
}
