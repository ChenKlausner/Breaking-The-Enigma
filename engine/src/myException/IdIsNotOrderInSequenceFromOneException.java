package myException;

public class IdIsNotOrderInSequenceFromOneException extends RuntimeException{
    private final String EXCEPTION_MESSAGE = "The rotors id's is not order in a Sequence starting from 1.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
