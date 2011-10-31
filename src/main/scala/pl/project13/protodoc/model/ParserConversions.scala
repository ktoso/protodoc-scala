package pl.project13.protodoc.model

/**
 *
 * @author Konrad Malawski
 */
trait ParserConversions {

  implicit def list2enumTypeList(li: List[Any]): List[ProtoEnumType] = {
    li.filter(_.isInstanceOf[ProtoEnumType]).map(_.asInstanceOf[ProtoEnumType])
  }

  implicit def list2messageFieldList(li: List[Any]): List[ProtoMessageField] = {
    li.filter(_.isInstanceOf[ProtoMessageField]).map(_.asInstanceOf[ProtoMessageField])
  }

  implicit def list2messageList(li: List[Any]): List[ProtoMessageType] = {
    li.filter(_.isInstanceOf[ProtoMessageType]).map(_.asInstanceOf[ProtoMessageType])
  }

  implicit def tag2long(tag: ProtoTag): Long = {
    tag.tagNumber
  }

  implicit def long2tag(tagNumber: Long): ProtoTag = {
    checkProtoTagNumber(tagNumber)

    ProtoTag(tagNumber)
  }

  implicit def str2tag(tagString: String): ProtoTag = {
    long2tag(tagString.toLong)
  }

  def checkProtoTagNumber(tagNumber: Long) = {
    if (!(tagNumber > 0 && tagNumber <= 536870911 && (tagNumber < 1900 || tagNumber > 1999))) {
      throw new RuntimeException("Invalid tag: " + tagNumber)
    }
  }

}