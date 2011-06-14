package pl.project13.protodoc.model

import java.lang.UnsupportedOperationException

/**
 *
 */
abstract class ProtoMessageField(val fieldName: String,
                                 val defaultValue: Any = null)

/**
 * Represent an Int property
 */
case class IntProtoMessageField(override val fieldName: String,
                                override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an Int property
 */
case class LongProtoMessageField(override val fieldName: String,
                                 override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an String property
 */
case class BooleanProtoMessageField(override val fieldName: String,
                                       override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an Float property
 */
case class FloatProtoMessageField(override val fieldName: String,
                                  override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an Double property
 */
case class DoubleProtoMessageField(override val fieldName: String,
                                   override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an String property
 */
case class StringProtoMessageField(override val fieldName: String,
                                   override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an String property
 */
case class ByteStringProtoMessageField(override val fieldName: String,
                                       override val defaultValue: Any = null)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField {
  def toTypedField(typeName: String, fieldName: String, defaultValue: Any) = typeName match {
    case "int" | "int32" | "uint32" | "sint32" =>
      new IntProtoMessageField(fieldName, defaultValue)
    case "long" | "int64" | "uint64" | "sint64" =>
      new LongProtoMessageField(fieldName, defaultValue)
    case "double" =>
      new DoubleProtoMessageField(fieldName, defaultValue)
    case "float" =>
          new FloatProtoMessageField(fieldName, defaultValue)
    case "bool" =>
          new BooleanProtoMessageField(fieldName, defaultValue)
    case "string" =>
      new StringProtoMessageField(fieldName, defaultValue)
    case "bytes" =>
      new ByteStringProtoMessageField(fieldName, defaultValue)
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: " + unknownType)
  }
}