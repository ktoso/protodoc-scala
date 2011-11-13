package pl.project13.protodoc.verifier

import pl.project13.protodoc.model.{ProtoType, ProtoMessageType}


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
  def isDefinedWithin(context: ProtoMessageType) = context.innerMessages.map { _.messageName } contains typeNameToResolve

  def getResolvedTypeWithin(context: ProtoMessageType) = context.innerMessages.find { _.messageName == typeNameToResolve }
}