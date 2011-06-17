package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class RequiredFieldMayNotHaveDefaultValueException extends RuntimeException {

  public RequiredFieldMayNotHaveDefaultValueException(String fieldName) {
    super("The field " + fieldName + " is invalid. 'Required' fields may not have default values");
  }

}
