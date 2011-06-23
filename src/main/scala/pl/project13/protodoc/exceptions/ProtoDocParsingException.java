package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class ProtoDocParsingException extends RuntimeException {

  private static final long serialVersionUID = 771309554619672374L;

  public ProtoDocParsingException() {
  }

  public ProtoDocParsingException(String message) {
    super(message);
  }

  public ProtoDocParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
