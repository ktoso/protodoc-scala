package pl.project13.protodoc.exceptions;

import pl.project13.protodoc.VerificationResult;

public class ProtoDocVerificationException extends RuntimeException {

  public final VerificationResult verificationResult;

  public ProtoDocVerificationException(VerificationResult verificationResult) {
      super();
    this.verificationResult = verificationResult;
  }

  @Override
  public String toString() {
    return verificationResult.toString();
  }
}
