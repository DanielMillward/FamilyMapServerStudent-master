package MyExceptions;

/**
 * Thrown if the given parameters are missing/incorrect
 */
public class InvalidInputException extends Exception{
    public InvalidInputException(String s) {
        super(s);
    }
    public InvalidInputException() {}
}
