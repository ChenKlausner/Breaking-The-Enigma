package myException;

public class AlphabetNotEvenLengthException extends RuntimeException {
    private final String EXCEPTION_MESSAGE = "Alphabet length IS NOT even!";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
