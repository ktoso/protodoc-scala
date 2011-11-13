package pl.project13.protodoc.exceptions;

/**
 * @author Konrad Malawski
 */
public class ProtoDocCompilerException extends RuntimeException {

  private static final long serialVersionUID = 771309554619672374L;

  public ProtoDocCompilerException() {
  }

  public ProtoDocCompilerException(String message) {
    super(message);
  }

  public ProtoDocCompilerException(String message, Throwable cause) {
    super(message, cause);
  }
}
