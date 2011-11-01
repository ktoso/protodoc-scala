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
  def verify(protoTypes: List[ProtoType]): VerificationResult = {
    val errorLists: Seq[Seq[VerifierError]] = for (protoType <- protoTypes) yield check(protoType, protoTypes)
    val errors: Seq[VerifierError] = errorLists.flatten

    errors.foreach(error(_))

    VerificationResult(errors)
  }

  def check[T <: ProtoType](protoType: T, protoTypes: List[T]): List[VerifierError] = protoType match {
    case msgType: ProtoMessageType =>
      info("Running verifications on "+b(msgType.fullName)+" message")

      val enumErrors = for (enum <- msgType.enums) yield checkEnumType(enum, protoTypes) // todo cast? and validate each kind of field
      val fieldErrors = for (field <- msgType.fields) yield checkField(msgType, field, protoTypes)
      val innerMsgErrors = for (innerMsg <- msgType.innerMessages) yield checkInnerMsg(msgType, innerMsg, protoTypes)
      // todo more checks

      fieldErrors.flatten ::: enumErrors.flatten ::: innerMsgErrors.flatten ::: Nil

    case enumType: ProtoEnumType =>
      info("Running verifications on "+b(enumType.fullName)+" enum")

      var enumErrors = List()
      
      val enumValues = enumType.values
      val tagUniquenessErrors = TagVerifier.validateTagUniqueness(enumType, enumValues.map(_.tag))

      enumErrors ::: tagUniquenessErrors ::: Nil

    case _ =>
      List() // empty errors list
  }

  /**
   * Check if an enum has valid values etc
   */
  def checkEnumType(enum: ProtoEnumType, protoTypes: List[ProtoType]): List[VerifierError] = {
    // todo implement me
    info("Checking enum "+enum+" for errors...")

    List()
  }

  /**
   * Check if an enum exists (is in scope)
   */
  def checkField(context: ProtoMessageType,
                 field: ProtoMessageField, 
                 protoTypes: List[ProtoType]): List[VerifierError] = {
    info("Checking field "+b(field)+" in "+b(context.fullName)+" context for errors...")
    
    if(field.unresolvedType) {
      info("Type is still unresolved. Trying to resolve protoTypeName: "+b(field.protoTypeName))
    }
    
    
    List()
  }

  /**
   * Check an inner message
   */
  def checkInnerMsg(context: ProtoMessageType, 
                    selfMsg: ProtoMessageType, 
                    protoTypes: List[ProtoType]): List[VerifierError] = {
    info("Checking inner message "+selfMsg.fullName+" for errors...")
    
    List()
  }
}