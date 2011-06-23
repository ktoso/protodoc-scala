package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class InvalidModifierException extends RuntimeException {

  private static final long serialVersionUID = -5278899620537400987L;

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
