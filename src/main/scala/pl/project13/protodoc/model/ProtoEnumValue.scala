package pl.project13.protodoc.model

/**
 * Represents a value of an enum field
 */
case class ProtoEnumValue(valueName: String, tag: ProtoTag)
  extends ProtoField
  with Commentable
  with Deprecatable