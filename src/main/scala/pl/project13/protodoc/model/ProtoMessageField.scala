package pl.project13.protodoc.model

import java.lang.UnsupportedOperationException

/**
 *
 */
abstract class ProtoMessageField(fieldName: String, defaultValue: Any = null)

/**
 * Represent an Int property
 */
class IntProtoMessageField(fieldName: String, defaultValue: Any)
  extends ProtoMessageField(fieldName, defaultValue)

/**
 * Represent an Double property
 */
class DoubleProtoMessageField(fieldName: String, defaultValue: Any)
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
      println("Found an double field")
      new DoubleProtoMessageField(fieldName, defaultValue)
    case unknownType =>
      throw new UnsupportedOperationException("Unknown field type encountered: " + unknownType)
  }
}