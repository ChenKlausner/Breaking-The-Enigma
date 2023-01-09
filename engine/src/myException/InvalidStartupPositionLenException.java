package myException;

public class InvalidStartupPositionLenException extends RuntimeException{
    private int numOfRotors;
    private int rotorCount;

    private final String EXCEPTION_MESSAGE = "Invalid input. There is %d characters but you should enter a sequence of %d characters.";

    public InvalidStartupPositionLenException(int numOfRotors, int rotorCount) {
        this.numOfRotors = numOfRotors;
        this.rotorCount = rotorCount;
    }

    public int getNumOfRotors() {
        return numOfRotors;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, numOfRotors, rotorCount);
    }
}
