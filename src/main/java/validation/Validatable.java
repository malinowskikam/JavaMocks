package validation;

public interface Validatable
{
    boolean isValid();
    String getValidationError();
}