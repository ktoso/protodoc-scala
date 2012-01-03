package pl.project13.protodoc.model

import ProtoModifier._
import ProtoMessageField.Empty
import pl.project13.protodoc.utils.CommonImplicits

/**
 * The base class for all other basic ProtoMessageFields, and also for itself, when a userdefined type is used.
 * @param unresolvedType is used to mark that this fields type was unable to link during parsing,
 *        the {@link ProtoBufVerifier} should take care of resolving this fields type.
 *
 * @author Konrad Malawski
 */
class ProtoMessageField(
    val fieldName:      String,
    var protoTypeName:  String,
    var scalaTypeName:  String,
    val tag:            ProtoTag,
    val modifier:       ProtoModifier,
    val defaultValue:   Any = None,
    var unresolvedType: Boolean = false)
  extends ProtoField
  with Commentable
  with Deprecatable {

  // todo may need to know if it's a message or enum???
  // todo will have to be improved when we allow option java_package etc
  def resolveTypeTo(resolvedType: ProtoType): ProtoMessageField = asResolvedType(resolvedType.fullName, resolvedType.fullName)

  def asResolvedType(fullyQualifiedProtoTypeName: String,
                    fullyQualifiedScalaTypeName: String) = {
    unresolvedType = false
    protoTypeName = fullyQualifiedProtoTypeName
    scalaTypeName = fullyQualifiedScalaTypeName

    this
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
                                override val defaultValue: Int = 0)
  extends ProtoMessageField(fieldName, theProtoTypeName, "scala.Int", tag, modifier, defaultValue)

/**
 * Represent an Int property
 */
case class LongProtoMessageField(override val fieldName:    String,
                                 theProtoTypeName:          String,
                                 override val tag:          ProtoTag,
                                 override val modifier:     ProtoModifier,
                                 override val defaultValue: Long = 0)
  extends ProtoMessageField(fieldName, theProtoTypeName, "scala.Long", tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class BooleanProtoMessageField(override val fieldName: String,
                                    override val tag: ProtoTag,
                                    override val modifier: ProtoModifier,
                                    override val defaultValue: Boolean = false)
  extends ProtoMessageField(fieldName, "bool", "scala.Boolean", tag, modifier, defaultValue)

/**
 * Represent an Float property
 */
case class FloatProtoMessageField(override val fieldName: String,
                                  override val tag: ProtoTag,
                                  override val modifier: ProtoModifier,
                                  override val defaultValue: Float = 0)
  extends ProtoMessageField(fieldName, "float", "scala.Float", tag, modifier, defaultValue)

/**
 * Represent an Double property
 */
case class DoubleProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: Double = 0)
  extends ProtoMessageField(fieldName, "double", "scala.Double", tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class StringProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: String = Empty)
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
//                                 override val defaultValue: ProtoEnumValue) // todo
  extends ProtoMessageField(fieldName, theScalaTypeName, theProtoTypeName,  tag, modifier, defaultValue)

/**
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField extends ProtoTagConversions {

  val Empty = ""

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
      IntProtoMessageField(fieldName, typeName, protoTag, modifier, defaultValue.getOrElse(0).toString.toInt)
    case "int64" | "uint64" | "sint64" | "fixed64" | "sfixed64" =>
      LongProtoMessageField(fieldName, typeName, protoTag, modifier, defaultValue.getOrElse(0).toString.toInt)
    case "double" =>
      DoubleProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(0).toString.toDouble)
    case "float" =>
      FloatProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(0).toString.toFloat)
    case "bool" =>
      BooleanProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse("false").toString.toBoolean)
    case "string" =>
      StringProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse("").toString)
    case "bytes" =>
      ByteStringProtoMessageField(fieldName, protoTag, modifier, defaultValue.getOrElse(None))
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: "+unknownType)
  }

  def toEnumField(fieldName: String,
                  isValueOfEnumType: ProtoEnumType,
                  protoTag: Int,
                  modifier: ProtoModifier,
                  defaultValue: Option[Any] = None) = {
    EnumProtoMessageField(fieldName = fieldName,
                          theProtoTypeName = isValueOfEnumType.typeName,
                          theScalaTypeName = isValueOfEnumType.typeName,
                          tag = protoTag,
                          modifier = modifier,
                          defaultValue = defaultValue.getOrElse(None))
  }
}