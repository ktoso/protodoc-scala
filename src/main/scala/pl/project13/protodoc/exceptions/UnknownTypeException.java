package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class UnknownTypeException extends RuntimeException {

  private static final long serialVersionUID = -1489605228034592169L;

  public UnknownTypeException() {
  }

  public UnknownTypeException(String message) {
    super(message);
  }

  public UnknownTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnknownTypeException(Throwable cause) {
    super(cause);
  }
}
