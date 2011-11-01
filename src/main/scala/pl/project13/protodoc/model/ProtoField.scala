package pl.project13.protodoc.model


/**
 * Used as common interface between enum values and message fields
 */
abstract class ProtoField {
  val tag: ProtoTag
}