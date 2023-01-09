package myException;

public class RotorHasDoubleMappingException extends RuntimeException {
    private int rotorId;
    private Character signal;

    private final String EXCEPTION_MESSAGE = "Rotor #%d has double mapping for the character %c.";

    public RotorHasDoubleMappingException(int rotorId, Character signal) {
        this.rotorId = rotorId;
        this.signal = signal;
    }

    public int getRotorId() {
        return rotorId;
    }

    public Character getSignal() {
        return signal;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, rotorId, signal);
    }
}
