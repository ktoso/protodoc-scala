package pl.project13.protodoc

import collection.JavaConversions._
import scala.List.empty

/**
 * Used to indicate if a set of ProtoTypes was valid or not
 */
case class VerificationResult(errors: Seq[VerificationError] = empty) extends {

  def valid = !invalid
  def invalid = errors.length > 0

  def getErrors: java.util.List[VerificationError] = errors
}
