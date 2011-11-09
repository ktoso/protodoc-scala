package pl.project13.protodoc.model

import ProtoModifier._

/**
 * The base class for all other basic ProtoMessageFields, and also for itself, when a userdefined type is used.
 * @param unresolvedType is used to mark that this fields type was unable to link during parsing,
 *        the {@link ProtoBufVerifier} should take care of resolving this fields type.
 *
 * @author Konrad Malawski
 */
class ProtoMessageField(val fieldName:      String,
                        var protoTypeName:  String,
                        var scalaTypeName:  String,
                        val tag:            ProtoTag,
                        val modifier:       ProtoModifier,
                        val defaultValue:   Any = None,
                        var unresolvedType: Boolean = false)
                        extends ProtoField
                           with Commentable {

  // todo may need to know if it's a message or enum???
  // todo will have to be improved when we allow option java_package etc
  def resolveTypeTo(resolvedType: ProtoType): ProtoMessageField = asResolvedType(resolvedType.fullName, resolvedType.fullName)

  def asResolvedType(fullyQualifiedProtoTypeName: String,
                    fullyQualifiedScalaTypeName: String) = {
    unresolvedType = false
    protoTypeName = fullyQualifiedProtoTypeName
    scalaTypeName = fullyQualifiedScalaTypeName

    this

//      new ProtoMessageField(fieldName, fullyQualifiedProtoTypeName, fullyQualifiedScalaTypeName, tag,
//                                modifier, defaultValue)
  }

  val protoFormat = {
    (modifier, protoTypeName)
  }

  override def toString = "ProtoMessageField["+fieldName+": "+protoTypeName+"]"
}

/**
 * Represent an Int property
 */
case class IntProtoMessageField(override val fieldName:    String,
                                theProtoTypeName:          String,
                                override val tag:          ProtoTag,
                                override val modifier:     ProtoModifier,
                                override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, theProtoTypeName, "scala.Int", tag, modifier, defaultValue)

/**
 * Represent an Int property
 */
case class LongProtoMessageField(override val fieldName:    String,
                                 theProtoTypeName:          String,
                                 override val tag:          ProtoTag,
                                 override val modifier:     ProtoModifier,
                                 override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, theProtoTypeName, "scala.Long", tag, modifier, defaultValue)

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
 * Represent a Enum property, that is of course also defined as Protocol Buffers resource
 */
case class EnumProtoMessageField(override val fieldName: String,
                                 theProtoTypeName: String,
                                 theScalaTypeName: String,
                                 override val tag: ProtoTag,
                                 override val modifier: ProtoModifier,
                                 override val defaultValue: Any)
//                                 override val defaultValue: ProtoEnumValue) // todo would be awesome
  extends ProtoMessageField(fieldName, theScalaTypeName, theProtoTypeName,  tag, modifier, defaultValue)

/**
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField extends ProtoTagConversions {

  def toUnresolvedField(typeName: String,
                        fieldName: String,
                        protoTag: Int,
                        modifier: ProtoModifier,
                        defaultValue: Option[Any] = None) = {
    new ProtoMessageField(fieldName, typeName, typeName/*todo should be fully qualified?*/, protoTag, modifier, defaultValue.getOrElse(None), true)
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
      throw new UnsupportedOperationException("Unknown field type encountered: "+unknownType)
  }

  def toEnumField(fieldName: String,
                  isValueOfEnumType: ProtoEnumType,
                  protoTag: Int,
                  modifier: ProtoModifier,
                  defaultValue: Option[Any] = None) = {
//                  defaultValue: Option[ProtoEnumValue] = None) = { // todo would be awesome
    new EnumProtoMessageField(fieldName = fieldName,
                              theProtoTypeName = isValueOfEnumType.typeName,
                              theScalaTypeName = isValueOfEnumType.typeName,
                              tag = protoTag,
                              modifier = modifier,
                              defaultValue = defaultValue.getOrElse(null))
  }
}