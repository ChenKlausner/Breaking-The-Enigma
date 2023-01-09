package enigma;

import component.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Enigma implements Serializable {
    private Keyboard keyboard;
    private Plugboard plugboard;
    private List<Rotor> rotorsList;
    private List<Reflector> reflectorsList;
    private final int rotorCount;
    private List<Rotor> rotorsInUse;
    private Reflector reflector;
    private int messageCounter;


    public Enigma(Keyboard keyboard, List<Rotor> rotorsList, List<Reflector> reflectorsList, int rotorCount) {
        this.keyboard = keyboard;
        this.plugboard = null;
        this.rotorsList = rotorsList;
        this.reflectorsList = reflectorsList;
        this.rotorCount = rotorCount;
        this.rotorsInUse = null;
        this.reflector = null;
        this.messageCounter = 0;
    }
    public List<Rotor> getRotorsList() {
        return rotorsList;
    }

    public List<Rotor> getRotorsInUse() {
        return rotorsInUse;
    }

    public List<Reflector> getReflectorsList() {
        return reflectorsList;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Plugboard getPlugboard() {
        return plugboard;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public int getRotorCount() {
        return rotorCount;
    }

    public Reflector getReflector() {
        return reflector;
    }

    public void setToStartPosition() {
        IntStream.range(0, rotorsInUse.size()).forEach(i -> rotorsInUse.get(i).initToStartPosition());
    }

    public void setToZeroPosition(){
        for (int i = 0; i < rotorsInUse.size(); i++) {
            rotorsInUse.get(i).setToZeroPosition(keyboard.getAlphabet().charAt(0));
        }
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    public void rotate() {
        rotorsInUse.get(0).rotate();
        if (rotorsInUse.get(0).isAtNotch()) {
            rotorsInUse.get(1).rotate();
            IntStream
                    .range(1, rotorsInUse.size() - 1)
                    .filter(i -> rotorsInUse.get(i).isAtNotch())
                    .forEach(i -> rotorsInUse.get(i + 1).rotate());
        }
    }

    public void advance(int steps){
        for (int i = 0; i < steps; i++) {
            rotorsInUse.get(0).rotate();
            if (rotorsInUse.get(0).isInZeroPosition(keyboard.getAlphabet().charAt(0))){
                rotorsInUse.get(1).rotate();
                for (int j = 1; j < rotorsInUse.size() - 1; j++) {
                    if (rotorsInUse.get(j).isInZeroPosition(keyboard.getAlphabet().charAt(0))) {
                        rotorsInUse.get(j + 1).rotate();
                    }
                }
            }
        }
    }

    public char encryptLetter(char inputChar) {
        char outputChar = plugboard.swapCharacters(inputChar);
        int indexOfCurrChar = keyboard.getPositionOfCharacter(outputChar);
        this.rotate();
        for (int i = 0; i < rotorsInUse.size(); i++) {
            indexOfCurrChar = rotorsInUse.get(i).getForwardPosition(indexOfCurrChar);
        }
        indexOfCurrChar = reflector.reflect(indexOfCurrChar);
        for (int i = rotorsInUse.size() - 1; i >= 0; i--) {
            indexOfCurrChar = rotorsInUse.get(i).getBackwardPosition(indexOfCurrChar);
        }
        outputChar = keyboard.getAlphabet().charAt(indexOfCurrChar);
        outputChar = plugboard.swapCharacters(outputChar);
        return outputChar;
    }

    public String encrypt(String input) {
        String output = IntStream
                                .range(0, input.length())
                                .mapToObj(i -> String.valueOf(encryptLetter(input.charAt(i))))
                                .collect(Collectors.joining());
        return output;
    }

    public void setPlugboard(Plugboard plugboard) {
        this.plugboard = plugboard;
    }

    public void setRotorsInUse(List<Rotor> rotorsInUse) {
        this.rotorsInUse = rotorsInUse;
    }

    public void setReflector(Reflector reflector) {
        this.reflector = reflector;
    }
}
