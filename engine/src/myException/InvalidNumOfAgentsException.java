package myException;

public class InvalidNumOfAgentsException extends RuntimeException {
    private final String EXCEPTION_MESSAGE = "Invalid Number of agents , Number of agents should be between 2 to 50!";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
