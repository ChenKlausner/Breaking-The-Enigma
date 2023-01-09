package myException;

public class CharMapToItselfException extends RuntimeException {
    private String id;
    private int input;

    private final String EXCEPTION_MESSAGE = "Reflector %s maps entry %d to itself.";

    public CharMapToItselfException(String id, int input) {
        this.id = id;
        this.input = input;
    }

    public String getId() {
        return id;
    }

    public int getInput() {
        return input;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, id, input);
    }
}
