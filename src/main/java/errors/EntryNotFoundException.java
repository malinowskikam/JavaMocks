package errors;

public class EntryNotFoundException extends Exception
{
    public EntryNotFoundException(String model,Long id)
    {
        super("There is no " + model + " with id " + id);
    }
}
