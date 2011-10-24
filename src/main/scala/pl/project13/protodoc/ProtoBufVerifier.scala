package pl.project13.protodoc

import model._

/**
 * @author Konrad Malawski
 */
object ProtoBufVerifier extends Logger {

  def verify(protoTypes: List[ProtoType]) {
    val errors: List[VerifierError] = for (protoType <- protoTypes) yield check(protoType, protoTypes)

    errors.foreach(error(_))
  }

  def check(protoType: ProtoType, protoTypes: List[ProtoType]): List[VerifierError] = protoType match {
    case ProtoMessage =>
      val msgType = protoType.asInstanceOf[ProtoMessage]

      val enumErrors = for (enum <- msgType.enums) yield check(enum, protoTypes) // todo cast? and validate each kind of field
      val fieldErrors = for (field <- msgType.fields) yield check(field, protoTypes)

      val innerMsgErrors = for (innerMsg <- msgType.innerMessages) yield check(protoType, protoTypes) // recursive is ok?
      // todo more checks

      fieldErrors ::: enumErrors ::: innerMsgErrors ::: Nil
    case _ =>
      List() // empty errors list
  }

  /**
   * Check if an enum has valid values etc
   */
  def check(enum: ProtoEnumType, protoTypes: List[ProtoType]) {
    // todo implement me
  }

  /**
   * Check if an enum exists (is in scope)
   */
  def check(field: ProtoMessageField, protoTypes: List[ProtoType]) {
    // todo implement me
  }

  // some ansi helpers --------------------------------------------------------
  def ANSI(value: Any) = "\u001B[" + value + "m"

  val BOLD = ANSI(1)
  val RESET = ANSI(0)
}