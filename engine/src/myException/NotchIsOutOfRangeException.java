package myException;

public class NotchIsOutOfRangeException extends RuntimeException{
    private int rotorId;
    private int alphabetLength;

    private final String EXCEPTION_MESSAGE = "Notch in rotor #%d is out of range , should be between 1 to %d.";

    public NotchIsOutOfRangeException(int rotorId, int alphabetLength) {
        this.rotorId = rotorId;
        this.alphabetLength = alphabetLength;
    }


    public int getRotorId() {
        return rotorId;
    }

    public int getAlphabetLength() {
        return alphabetLength;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, rotorId, alphabetLength);
    }
}
