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
 * Companion object, serves as factory, will be used by parser
 */
object ProtoMessageField {
  def toTypedField(typeName: String, fieldName: String, defaultValue: Any) = typeName match {
    case "int" =>
    case "int32" =>
    case "int64" =>
      println("Found an int field")
      new IntProtoMessageField(fieldName, defaultValue)
    case "double" =>
      println("Found a double field")
      new DoubleProtoMessageField(fieldName, defaultValue)
    case "string" =>
      println("Found a string field")
      new StringProtoMessageField(fieldName, defaultValue)
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: " + unknownType)
  }
}