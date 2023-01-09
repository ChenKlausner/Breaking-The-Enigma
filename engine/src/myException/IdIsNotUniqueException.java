package myException;

public class IdIsNotUniqueException extends RuntimeException{
    private int id;

    private final String EXCEPTION_MESSAGE = "There is two rotors with the same id number -(Rotor id: %d).";

    public IdIsNotUniqueException(int id) {
        this.id = id;
    }

    public int getNumOfRotors() {
        return id;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, id);
    }
}
