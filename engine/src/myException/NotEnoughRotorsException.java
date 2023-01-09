package myException;

public class NotEnoughRotorsException extends RuntimeException {
    private int numOfRotors;
    private int rotorCount;

    private final String EXCEPTION_MESSAGE = "Invalid number of rotors. The machine needs %d rotors but there are only %d rotors available.";

    public NotEnoughRotorsException(int numOfRotors, int rotorCount) {
        this.numOfRotors = numOfRotors;
        this.rotorCount = rotorCount;
    }

    public int getNumOfRotors() {
        return numOfRotors;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, rotorCount, numOfRotors);
    }
}
