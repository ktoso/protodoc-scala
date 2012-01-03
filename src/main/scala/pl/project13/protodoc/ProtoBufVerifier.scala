package pl.project13.protodoc

import scala.List.empty
import model._
import verifier.VerifierConversions._
import verifier._

/**
 * @author Konrad Malawski
 */
object ProtoBufVerifier extends Logger {

  val NoErrorsEncountered = empty

  /**
   * Checks ProtoTypes for errors (invalid type references etc) and return true if a fileset is valid.
   *
   * Logs error messages to the console
   * @return true if the passed in ProtoTypes are valid, false otherwise
   */
  def verify(protoTypes: List[ProtoType]): VerificationResult = {
    val errors = (for (protoType <- protoTypes)
                    yield check(protoType, protoTypes)).flatten

    VerificationResult(errors)
  }

  def check[T <: ProtoType](protoType: T, protoTypes: List[T]): List[VerificationError] = protoType match {
    case msgType: ProtoMessageType =>
      checkMessageType(msgType, protoTypes)

    case enumType: ProtoEnumType =>
      checkEnumType(enumType, protoTypes)
  }
  
  def checkMessageType(msgType: ProtoMessageType, protoTypes: List[ProtoType]) = {
    info("Running verifications on "+b(msgType)+" message")

    // errors 
    val tagErrors = TagVerifier.validateTags(msgType, msgType.fields.map(_.tag))

    val enumErrors = for (enum <- msgType.enums) yield checkEnumType(enum, protoTypes)
    val fieldErrors = for (field <- msgType.fields) yield checkField(msgType, field, protoTypes)
    val innerMsgErrors = for (innerMsg <- msgType.innerMessages) yield checkInnerMsg(msgType, innerMsg, protoTypes)

    val duplicationErrors = for(t <-protoTypes) yield checkIfDuplicated(msgType, protoTypes)
    
    // warnings 
    val deprecatedItemsCount = checkDeepDeprecation(msgType)
    warn("Found "+deprecatedItemsCount+" deprecated fields/types in ["+msgType.fullName+"]")

    // todo more checks

    tagErrors :::
      fieldErrors.flatten :::
      enumErrors.flatten :::
      duplicationErrors.flatten :::
      innerMsgErrors.flatten
  }
  
  def checkDeepDeprecation(msgType: ProtoMessageType): Long = {
    val typeDeprecated = checkDeprecation(msgType)
    val deprecationOnFields = for (field <- msgType.fields) yield checkDeprecation(field)
    val deprecationInnerMessages = for (field <- msgType.innerMessages) yield checkDeprecation(field)
    val deprecationInnerEnums = for (field <- msgType.enums) yield checkDeprecation(field)
    
    val deprecations = deprecationOnFields.count { deprecated => deprecated } +
      deprecationInnerMessages.count { deprecated => deprecated } +
      deprecationInnerEnums.count { deprecated => deprecated } + (if(typeDeprecated) 1 else 0)

    deprecations
  }

  
  def checkDeprecation(it: Commentable with Deprecatable): Boolean = {
    val DeprecatedAnnotation = "@deprecated"

    val deprecated: Boolean = it.comment.contains(DeprecatedAnnotation)

    if (deprecated) warn(it + " is deprecated!")
    it.deprecated = deprecated
    deprecated
  }
  
  /**
   * Check if an enum has valid values etc
   */
  def checkEnumType(enumType: ProtoEnumType, protoTypes: List[ProtoType]): List[VerificationError] = {
    info("Running verifications on enum "+b(enumType)+"")

    // errors
    // todo add more checks

    val enumValues = enumType.values
    val tagUniquenessErrors = TagVerifier.validateTags(enumType, enumValues.map(_.tag))
    val duplicationErrors = for(t <-protoTypes) yield checkIfDuplicated(enumType, protoTypes)

    // warnings
    checkDeprecation(enumType)
    checkIfEmpty(enumType)

    tagUniquenessErrors ::: duplicationErrors.flatten
  }
  
  def checkIfEmpty(enumType: ProtoEnumType) {
    if(enumType.values.isEmpty)
      warn("The enum "+enumType.fullName+" has no values. This could be a possible typo with missplacing the }, please check." )
  }

  /**
   * Check if an enum exists (is in scope)
   */
  def checkField(context: ProtoMessageType, 
                 field: ProtoMessageField, 
                 protoTypes: List[ProtoType]): List[VerificationError] = {
    var errors: List[VerificationError] = Nil

    info("Checking field "+b(field)+" in "+b(context)+" context for errors...")

    if (field.unresolvedType && context.representationOf == "message") {
      info("Type is still unresolved. Trying to resolve protoTypeName: " + b(field.protoTypeName))
      
      errors = errors ::: checkFieldTypeVisible(field = field, fromContext = context, allParsed = protoTypes)
    }
    
    errors
  }

  // todo should understand imports, lets add Imports to prototype?
  def checkFieldTypeVisible(field: ProtoMessageField, 
                            fromContext: ProtoMessageType,
                            allParsed: List[ProtoType]): List[UndefinedTypeVerificationError] = {
    val typeName = field.protoTypeName
    
    // try to find by fully qualified name
    val fullyQualifiedMatch = allParsed.find(_.fullName == typeName)

    if(fullyQualifiedMatch.isDefined) {
      field resolveTypeTo(fullyQualifiedMatch.get)
      
      return NoErrorsEncountered
    } 
    
    if(typeName isDefinedWithin fromContext) {
      val resolvedType = typeName getResolvedTypeWithin fromContext
      field resolveTypeTo(resolvedType)
      
      return NoErrorsEncountered
    }
    
    val resolvedType = typeName.isDefinedWithinSamePackage(fromContext, allParsed)
    if(resolvedType.isDefined) {
      field resolveTypeTo(resolvedType.get)

      return NoErrorsEncountered
    }

    // unable to resolve, report error
    error("the field: ["+field.fieldName+"] was unresolvable at this point...")
    UndefinedTypeVerificationError(field.fieldName, "Unable to resolve type ["+typeName+"] from ["+fromContext+"] context.") :: Nil
  }

  def checkIfDuplicated(context: ProtoType,
                        protoTypes: List[ProtoType]): List[VerificationError] = {
    val checkedTypeName = context.fullName
    val foundDuplicate = protoTypes.count { _.fullName == checkedTypeName } > 1

    if(foundDuplicate) DuplicateMessageVerificationError(checkedTypeName) :: Nil
    else Nil
  }

  /**
   * Check an inner message
   */
  def checkInnerMsg(context: ProtoMessageType, 
                    selfMsg: ProtoMessageType, 
                    protoTypes: List[ProtoType]): List[VerificationError] = {
    info("Checking inner message "+selfMsg.fullName+" for errors...")
    
    NoErrorsEncountered
  }
}