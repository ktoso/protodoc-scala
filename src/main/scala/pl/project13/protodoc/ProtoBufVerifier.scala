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
      checkMessageType(msgType, protoTypes)

    case enumType: ProtoEnumType =>
      checkEnumType(enumType, protoTypes)

    case _ =>
      warn("Got unsupported ProtoType: "+b(protoType)+", unable to verify.")
      List.empty // empty errors list
  }
  
  def checkMessageType(msgType: ProtoMessageType, protoTypes: List[ProtoType]) = {
    info("Running verifications on "+b(msgType)+" message")

    val tagErrors = TagVerifier.validateTags(msgType, msgType.fields.map(_.tag))

    val enumErrors = for (enum <- msgType.enums) yield checkEnumType(enum, protoTypes)
    val fieldErrors = for (field <- msgType.fields) yield checkField(msgType, field, protoTypes)
    val innerMsgErrors = for (innerMsg <- msgType.innerMessages) yield checkInnerMsg(msgType, innerMsg, protoTypes)
    // todo more checks

    tagErrors ::: fieldErrors.flatten ::: enumErrors.flatten ::: innerMsgErrors.flatten ::: Nil
  }

  /**
   * Check if an enum has valid values etc
   */
  def checkEnumType(enumType: ProtoEnumType, protoTypes: List[ProtoType]): List[VerifierError] = {
    // todo implement me
    info("Running verifications on enum "+b(enumType)+"")

    var enumErrors = List() // todo implement me

    val enumValues = enumType.values
    val tagUniquenessErrors = TagVerifier.validateTags(enumType, enumValues.map(_.tag))

    enumErrors ::: tagUniquenessErrors ::: Nil
  }

  /**
   * Check if an enum exists (is in scope)
   */
  def checkField(context: ProtoMessageType, 
                 field: ProtoMessageField, 
                 protoTypes: List[ProtoType]): List[VerifierError] = {
    var errors: List[VerifierError] = Nil

    info("Checking field "+b(field)+" in "+b(context)+" context for errors...")

    if (field.unresolvedType) {
      info("Type is still unresolved. Trying to resolve protoTypeName: " + b(field.protoTypeName))
      
      // todo should know about imports etc
      errors = errors ::: checkFieldTypeVisible(field = field, from = context, allParsed = protoTypes)
    }
    
    errors
  }

  // todo should understand imports, lets add Imports to prototype?
  def checkFieldTypeVisible(field: ProtoMessageField, 
                            from: ProtoType, 
                            allParsed: List[ProtoType]): List[UndefinedTypeVerifierError] = {
    val typeName = field.protoTypeName
    
    // try to find by fully qualified name
    val fullyQualifiedMatch = allParsed.find(_.fullName == typeName)

    if(fullyQualifiedMatch.isDefined) {
      field resolveTypeTo(fullyQualifiedMatch.get)
      List.empty
    } else {
      UndefinedTypeVerifierError(field.fieldName, "Unable to resolve type "+typeName+" from "+from+" context.") :: Nil
    }
    // todo check imports in all from
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