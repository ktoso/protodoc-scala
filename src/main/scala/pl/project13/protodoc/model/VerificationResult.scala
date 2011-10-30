package pl.project13.protodoc.model

import pl.project13.protodoc.VerifierError

/**
 * Used to indicate if a set of ProtoTypes was valid or not
 */
case class VerificationResult(errors: Seq[VerifierError] = List()) {

  def invalid = !valid

  def valid = errors != null && errors.size > 0
}