package pl.project13.protodoc.model

import ProtoModifier._
import pl.project13.protodoc.exceptions.RequiredFieldMayNotHaveDefaultValueException

/**
 *
 */
abstract class ProtoMessageField(val fieldName: String,
                                 val tag: ProtoTag,
                                 val modifier: ProtoModifier,
                                 val defaultValue: Any = None) {

  // assure the message field is valid
  modifier match {
    case RequiredProtoModifier() =>
      if(defaultValue != None)
        throw new RequiredFieldMayNotHaveDefaultValueException(fieldName)
  }
}

/**
 * Represent an Int property
 */
case class IntProtoMessageField(override val fieldName: String,
                                override val tag: ProtoTag,
                                override val modifier: ProtoModifier,
                                override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an Int property
 */
case class LongProtoMessageField(override val fieldName: String,
                                 override val tag: ProtoTag,
                                 override val modifier: ProtoModifier,
                                 override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class BooleanProtoMessageField(override val fieldName: String,
                                    override val tag: ProtoTag,
                                    override val modifier: ProtoModifier,
                                    override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an Float property
 */
case class FloatProtoMessageField(override val fieldName: String,
                                  override val tag: ProtoTag,
                                  override val modifier: ProtoModifier,
                                  override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an Double property
 */
case class DoubleProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class StringProtoMessageField(override val fieldName: String,
                                   override val tag: ProtoTag,
                                   override val modifier: ProtoModifier,
                                   override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Represent an String property
 */
case class ByteStringProtoMessageField(override val fieldName: String,
                                       override val tag: ProtoTag,
                                       override val modifier: ProtoModifier,
                                       override val defaultValue: Any = None)
  extends ProtoMessageField(fieldName, tag, modifier, defaultValue)

/**
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField extends HasProtoTag {
  def toTypedField(typeName: String,
                   fieldName: String,
                   protoTag: Any,
                   modifier: ProtoModifier,
                   defaultValue: Any = None) = typeName match {
    case "int" | "int32" | "uint32" | "sint32"| "fixed32" | "sfixed32" =>
      new IntProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "long" | "int64" | "uint64" | "sint64" | "fixed64" | "sfixed64" =>
      new LongProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "double" =>
      new DoubleProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "float" =>
          new FloatProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "bool" =>
          new BooleanProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "string" =>
      new StringProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case "bytes" =>
      new ByteStringProtoMessageField(fieldName, protoTag, modifier, defaultValue)
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: " + unknownType)
  }
}