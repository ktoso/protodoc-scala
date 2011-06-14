package pl.project13.protodoc.model

/**
 * 
 * @author Konrad Malawski
 */

trait ParserConversions {
  implicit def str2protoEnumValue(str: String): ProtoEnumValue = ProtoEnumValue(str)

  implicit def list2typedEnumTypeList(li: List[Any]): List[ProtoEnumTypeField] = {
    li.filter(_.isInstanceOf[ProtoEnumTypeField]).map(_.asInstanceOf[ProtoEnumTypeField])
  }

  implicit def list2typedMessageFieldList(li: List[Any]): List[ProtoMessageField] = {
    li.filter(_.isInstanceOf[ProtoMessageField]).map(_.asInstanceOf[ProtoMessageField])
  }
}