package myException;

public class SuffixIsNotXmlException extends RuntimeException{
    private final String EXCEPTION_MESSAGE = "The suffix of the path is not ending with .xml!";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
