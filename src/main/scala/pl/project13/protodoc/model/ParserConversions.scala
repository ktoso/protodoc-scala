package pl.project13.protodoc.model

/**
 * 
 * @author Konrad Malawski
 */

trait ParserConversions {
  implicit def str2protoEnumValue(str: String): ProtoEnumValue = ProtoEnumValue(str)
}