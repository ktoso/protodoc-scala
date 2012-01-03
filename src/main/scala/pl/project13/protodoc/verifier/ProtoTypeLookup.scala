package pl.project13.protodoc.verifier

import pl.project13.protodoc.model.{ProtoType, ProtoMessageType}
import pl.project13.protodoc.exceptions.{ProtoDocVerificationException}
import pl.project13.protodoc.{FieldVerificationError, VerificationError, VerificationResult}


/**
 * Fluent interface to check if a type is defined within some context.
 * For example:
 * <code>
 * <pre>
 *   message It {
 *     required MyEnum itsEnum = 213;
 *     
 *     enum MyEnum {
 *       VAL = 1;
 *     }
 *   }
 * </pre>
 * </code>
 * 
 * The context is It, thus, a lookup for MyEnum in the Context of It should pass,
 * even though the full name of MyEnum in this case is It.MyEnum.
 */
class ProtoTypeLookup(typeNameToResolve: String) {

  /**
   * Check if a type is resolvable within another type
   */
  def isDefinedWithin(context: ProtoMessageType) = {
    val innerMessageNames = context.innerMessages.map {_.messageName}
    val innerEnumNames = context.enums.map {_.typeName}

    (innerMessageNames contains typeNameToResolve) || (innerEnumNames contains typeNameToResolve)
  }

  /**
   * Check if the type is defined within the same package. Example:
   * <pre>
   * <code>
   *   package pl.project13;
   *
   *   message Msg {
   *     required MyEnumeration enumeration = 123;
   *   }
   *
   *   enum MyEnumeration { // so it should resolve to this type
   *     ONE = 1;
   *   }
   * </code>
   * </pre>
   */
  def isDefinedWithinSamePackage(context: ProtoMessageType, allTypes: List[ProtoType]): Option[ProtoType] = {
    val packageName = context.packageName
    val targetTypeFullName = packageName+"."+typeNameToResolve

    allTypes.find(_.fullName == targetTypeFullName)
  }

  @throws(classOf[ProtoDocVerificationException])
  def getResolvedTypeWithin(context: ProtoMessageType) = {
    val resolvedMessage = context.innerMessages.find {_.messageName == typeNameToResolve}
    val resolvedEnum = context.enums.find {_.typeName == typeNameToResolve}

    // check and throw if some problems found
    val bothDefined = resolvedMessage.isDefined && resolvedEnum.isDefined
    if(bothDefined) throwDueToDuplicateType(typeNameToResolve, context)

    val bothUndefined = resolvedMessage.isEmpty && resolvedEnum.isEmpty
    if(bothUndefined) throwDueToDuplicateType(typeNameToResolve, context)
    
    if(resolvedMessage.isDefined) {
      resolvedMessage.get
    } else {
      resolvedEnum.get
    }
  }

  @throws(classOf[ProtoDocVerificationException])
  private def throwDueToDuplicateType(typeNameToResolve: String, context: ProtoMessageType) {

    val msg = """|Unable to resolve ["""+typeNameToResolve+"""] due to a "duplicate resolution" of this type.
                 |Are you sure you didn't define this type two times in the same scope?""".stripMargin

    val error = FieldVerificationError(typeNameToResolve, msg) :: Nil
    val result = VerificationResult(error)

    throw new ProtoDocVerificationException(result)
  }
}