package errors;

public class ValidationException extends Exception
{
    public ValidationException(String model,String error)
    {
        super(model + ": " + error);
    }
}