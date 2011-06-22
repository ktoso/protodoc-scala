package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class InvalidModifierException extends RuntimeException {
  public InvalidModifierException() {
  }

  public InvalidModifierException(String message) {
    super(message);
  }

  public InvalidModifierException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidModifierException(Throwable cause) {
    super(cause);
  }
}
