package utility;

import java.io.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RotorsPositionsGenerator implements Serializable {
    private String alphabet;
    private Integer rotorCount;
    private LinkedList<LinkedList<Character>> rotorsPositions;
    private LinkedList<Character> startPosition;

    public RotorsPositionsGenerator(String alphabet, Integer rotorCount) {
        this.alphabet = alphabet;
        this.rotorCount = rotorCount;
        createRotorsPositions();
    }

    public void createRotorsPositions() {
        rotorsPositions = new LinkedList<>();
        IntStream.range(0, rotorCount).mapToObj(i -> IntStream.range(0, alphabet.length()).mapToObj(j -> alphabet.charAt(j)).collect(Collectors.toCollection(LinkedList::new))).forEachOrdered(singleRotor -> rotorsPositions.add(singleRotor));
    }

    public void initRotorsPositionsToZeroPosition() {
        for (int i = 0; i < rotorCount; i++) {
            while (rotorsPositions.get(i).getFirst() != alphabet.charAt(0)) {
                rotorsPositions.get(i).addLast(rotorsPositions.get(i).removeFirst());
            }
        }
    }

    public void advance(int steps) {
        for (int i = 0; i < steps; i++) {
            rotorsPositions.get(0).addLast(rotorsPositions.get(0).removeFirst());
            if (rotorsPositions.get(0).getFirst() == alphabet.charAt(0)) {
                rotorsPositions.get(1).addLast(rotorsPositions.get(1).removeFirst());
                for (int j = 1; j < rotorCount - 1; j++) {
                    if (rotorsPositions.get(j).getFirst() == alphabet.charAt(0)) {
                        rotorsPositions.get(j + 1).addLast(rotorsPositions.get(j + 1).removeFirst());
                    }
                }
            }
        }
    }

    public LinkedList<Character> getRotorsPositions(){
        LinkedList<Character> positions = new LinkedList<>();
        for (int i = 0; i < rotorsPositions.size(); i++) {
            positions.add(rotorsPositions.get(i).getFirst());
        }
        return positions;
    }

    public void setStartPosition(LinkedList<Character> startPosition) {
        this.startPosition = startPosition;
    }

    public void initializeToStartPosition(){
        for (int i = 0; i < rotorCount; i++) {
            while (rotorsPositions.get(i).getFirst() != startPosition.get(i)){
                rotorsPositions.get(i).addLast(rotorsPositions.get(i).removeFirst());
            }
        }
    }

    public RotorsPositionsGenerator getDeepCopy(){
        RotorsPositionsGenerator rotorsPositionsGenerator = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = Base64.getEncoder().encodeToString(baos.toByteArray());

        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            rotorsPositionsGenerator = (RotorsPositionsGenerator) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rotorsPositionsGenerator;
    }
}
