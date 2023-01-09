package myException;

public class InvalidRotorCountException extends RuntimeException{

    private final String EXCEPTION_MESSAGE = "Invalid rotor-count , rotors count should be at least 2.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
