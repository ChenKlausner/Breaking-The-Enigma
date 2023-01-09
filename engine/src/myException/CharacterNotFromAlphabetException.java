package myException;

public class CharacterNotFromAlphabetException extends RuntimeException{
    private final String EXCEPTION_MESSAGE = "Invalid input. The input contain characters not from the alphabet of the machine.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
