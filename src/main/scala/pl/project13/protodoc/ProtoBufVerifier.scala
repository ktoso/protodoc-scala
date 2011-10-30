package pl.project13.protodoc

import model._

/**
 * @author Konrad Malawski
 */
object ProtoBufVerifier extends Logger {

  /**
   * Checks ProtoTypes for errors (invalid type references etc) and return true if a fileset is valid.
   *
   * Logs error messages to the console
   * @return true if the passed in ProtoTypes are valid, false otherwise
   */
  def verify(protoTypes: Seq[ProtoType]): VerificationResult = {
    val errorLists: Seq[Seq[VerifierError]] = for (protoType <- protoTypes) yield check(protoType, protoTypes)
    val errors: Seq[VerifierError] = errorLists.flatten

    errors.foreach(error(_))

    VerificationResult(errors)
  }

  def check[T <: ProtoType](protoType: T, protoTypes: Seq[T]): Seq[VerifierError] = protoType match {
    case ProtoMessage =>
      val msgType = protoType.asInstanceOf[ProtoMessage]

      val enumErrors = for (enum <- msgType.enums) yield check(enum, protoTypes) // todo cast? and validate each kind of field
      val fieldErrors = for (field <- msgType.fields) yield check(field, protoTypes)

      val innerMsgErrors = for (innerMsg <- msgType.innerMessages) yield check(protoType, protoTypes) // recursive is ok?
      // todo more checks

      fieldErrors.flatten ::: enumErrors.flatten ::: innerMsgErrors.flatten ::: Nil
    case _ =>
      List() // empty errors list
  }

  /**
   * Check if an enum has valid values etc
   */
  def check(enum: ProtoEnumType, protoTypes: Seq[ProtoType]): Seq[VerifierError] = {
    // todo implement me

    List()
  }

  /**
   * Check if an enum exists (is in scope)
   */
  def check(field: ProtoMessageField, protoTypes: Seq[ProtoType]): Seq[VerifierError] = {
    // todo implement me
    List()
  }

}