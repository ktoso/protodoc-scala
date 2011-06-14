package pl.project13.protodoc.model

/**
 * 
 * @author Konrad Malawski
 */

case class ProtoEnumTypeField(typeName: String,
                              values: List[ProtoEnumValue])

case class ProtoEnumValue(valueName: String)