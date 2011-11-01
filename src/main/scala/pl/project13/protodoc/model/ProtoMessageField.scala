package pl.project13.protodoc.model

import ProtoModifier._

/**
 * The base class for all other basic ProtoMessageFields, and also for itself, when a userdefined type is used.
 * @author Konrad Malawski
 */
class ProtoMessageField(val fieldName: String,
                        val protoTypeName: String,
                        val scalaTypeName: String,
                        val tag: ProtoTag,
                        val modifier: ProtoModifier,
                        val defaultValue: Any = None,
                        var unresolvedType: Boolean = false)
                        extends ProtoField
                           with Commentable {

// todo protoc actually likes to get defaults here hmmm
//   assure the message field is valid
//  modifier match {
//    case RequiredProtoModifier() =>
//      if(defaultValue != None)
//        throw new RequiredFieldMayNotHaveDefaultValueException(fieldName, tag)
//  }
}

/**
 * Represent an Int property
 */
case class IntProtoMessageField(override val fieldName: String,
                                override val protoTypeName: String,
                                override val tag: ProtoTag,
                                override val modifier: ProtoModifier,
                                override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, protoTypeName, "scala.Int", tag, modifier, defaultValue)

/**
 * Represent an Int property
 */
case class LongProtoMessageField(override val fieldName: String,
                                 override val protoTypeName: String,
                                 override val tag: ProtoTag,
                                 override val modifier: ProtoModifier,
                                 override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, protoTypeName, "scala.Long", tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class BooleanProtoMessageField(override val fieldName: String,
                                    override val tag: ProtoTag,
                                    override val modifier: ProtoModifier,
                                    override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, "bool", "scala.Boolean", tag, modifier, defaultValue)

/**
 * Represent an Float property
 */
case class FloatProtoMessageField(override val fieldName: String,
                                  override val tag: ProtoTag,
                                  override val modifier: ProtoModifier,
                                  override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, "float", "scala.Float", tag, modifier, defaultValue)

/**
 * Represent an Double property
 */
case class DoubleProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, "double", "scala.Double", tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class StringProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, "string", "java.lang.String", tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class ByteStringProtoMessageField(override val fieldName: String,
                                       override val tag: ProtoTag,
                                       override val modifier: ProtoModifier,
                                       override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, "bytes", "com.google.protobuf.ByteString", tag, modifier, defaultValue)

/**
 * Represent a Message property thats type is not yet resolved, the verifier should check it
 */
case class UnresolvedMessageProtoMessageField(override val fieldName: String,
                                              override val protoTypeName: String,
                                              override val scalaTypeName: String,
                                              override val tag: ProtoTag,
                                              override val modifier: ProtoModifier,
                                              override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, scalaTypeName, protoTypeName,  tag, modifier, defaultValue, true)

/**
 * Represent a Enum property, that is of course also defined as Protocol Buffers resource
 */
case class EnumProtoMessageField(override val fieldName: String,
                                 override val protoTypeName: String,
                                 override val scalaTypeName: String,
                                 override val tag: ProtoTag,
                                 override val modifier: ProtoModifier,
                                 override val defaultValue: Any)
//                                 override val defaultValue: ProtoEnumValue) // todo would be awesome
  extends ProtoMessageField(fieldName, scalaTypeName, protoTypeName,  tag, modifier, defaultValue)

/**
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField extends ProtoTagConversions {

  def toUnresolvedField(typeName: String,
                        fieldName: String,
                        protoTag: Int,
                        modifier: ProtoModifier,
                        defaultValue: Option[Any] = None) = {
    new UnresolvedMessageProtoMessageField(fieldName, typeName, typeName/*todo should be fully qualified?*/, protoTag, modifier, defaultValue.getOrElse(None))
  }

  def toMessageField(typeName: String,
                     fieldName: String,
                     protoTag: Int,
                     modifier: ProtoModifier,
                     defaultValue: Option[Any] = None) = {
    new ProtoMessageField(fieldName, typeName, typeName/*todo should be fully qualified?*/, protoTag, modifier, defaultValue.getOrElse(None))
  }

  def toProtoField(typeName: String,
                   fieldName: String,
                   protoTag: Int,
                   modifier: ProtoModifier,
                   defaultValue: Option[Any] = None) = typeName match {
    case "int32" | "uint32" | "sint32"| "fixed32" | "sfixed32" =>
      new IntProtoMessageField(fieldName, typeName, protoTag, modifier, defaultValue.getOrElse(None))
    case "int64" | "uint64" | "sint64" | "fixed64" | "sfixed64" =>
      new LongProtoMessageField(fieldName, typeName, protoTag, modifier, defaultValue.getOrElse(None))
    case "double" =>
      new DoubleProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case "float" =>
      new FloatProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case "bool" =>
      new BooleanProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case "string" =>
      new StringProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case "bytes" =>
      new ByteStringProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: " + unknownType)
  }

  def toEnumField(fieldName: String,
                  isValueOfEnumType: ProtoEnumType,
                  protoTag: Int,
                  modifier: ProtoModifier,
                  defaultValue: Option[Any] = None) = {
//                  defaultValue: Option[ProtoEnumValue] = None) = { // todo would be awesome
    new EnumProtoMessageField(fieldName = fieldName,
                              protoTypeName = isValueOfEnumType.typeName,
                              scalaTypeName = isValueOfEnumType.typeName,
                              tag = protoTag,
                              modifier = modifier,
                              defaultValue = defaultValue.getOrElse(null))
  }
}