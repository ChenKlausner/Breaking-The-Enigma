package myException;

public class InvalidRotorIdException extends RuntimeException{
    private int id;

    private final String EXCEPTION_MESSAGE = "The machine does not contain rotor id #%d.";

    public InvalidRotorIdException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, id);
    }
}
