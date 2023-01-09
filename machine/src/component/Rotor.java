package component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Rotor implements Comparable<Rotor> , Serializable {
    private final int id;
    private LinkedList<Character> leftWiring;
    private LinkedList<Character> rightWiring;

    private final char notchPosition;
    private char startPosition;


    public Rotor(int id, LinkedList<Character> leftWiring, LinkedList<Character> rightWiring, int notchPosition) {
        this.id = id;
        this.leftWiring = leftWiring;
        this.rightWiring = rightWiring;
        this.notchPosition = rightWiring.get(notchPosition - 1);
    }

    public int getId() {
        return id;
    }

    public char getStartPosition() {
        return startPosition;
    }
    public LinkedList<Character> getRightWiring() {
        return rightWiring;
    }

    public void setStartPosition(char startPosition) {
        this.startPosition = startPosition;
    }

    public void initToStartPosition() {
        while (rightWiring.getFirst() != startPosition) {
            leftWiring.addLast(leftWiring.removeFirst());
            rightWiring.addLast(rightWiring.removeFirst());
        }
    }

    public void setToZeroPosition(char zeroPosition){
        while (leftWiring.getFirst() != zeroPosition) {
            leftWiring.addLast(leftWiring.removeFirst());
            rightWiring.addLast(rightWiring.removeFirst());
        }
    }

    public int getForwardPosition(int currentLetterPosition) {
        char currChar = rightWiring.get(currentLetterPosition);
        return leftWiring.indexOf(currChar);
    }

    public int getBackwardPosition(int currentLetterPosition) {
        char currChar = leftWiring.get(currentLetterPosition);
        return rightWiring.indexOf(currChar);
    }

    public void rotate() {
        leftWiring.addLast(leftWiring.removeFirst());
        rightWiring.addLast(rightWiring.removeFirst());
    }

    public boolean isAtNotch() {
        return this.rightWiring.getFirst() == notchPosition;
    }

    public int getNotchIntPosition() {
        for (int i = 0; i < rightWiring.size(); i++) {
            if (rightWiring.get(i) == notchPosition) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int compareTo(Rotor o) {
        return Integer.compare(id, o.id);
    }

    public boolean isInZeroPosition(char charAt) {
        return this.leftWiring.getFirst() == charAt;
    }
}
