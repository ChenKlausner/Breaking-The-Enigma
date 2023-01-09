package myException;

public class ReflrctorsIdOrderIsNotSequenceOfRomanException extends RuntimeException {
    private final String EXCEPTION_MESSAGE = "Id of reflector should be in sequence and unique from the above (I,II,III,IV,V) " +
            System.lineSeparator() + "please check the id's in reflector list or replace the file.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
