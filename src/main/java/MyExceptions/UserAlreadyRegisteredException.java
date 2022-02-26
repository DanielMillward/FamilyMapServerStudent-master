package MyExceptions;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException(String message)
    {
        super(message);
    }

    public UserAlreadyRegisteredException()
    {
        super();
    }
}
