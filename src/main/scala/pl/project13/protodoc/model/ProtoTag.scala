package pl.project13.protodoc.model

case class ProtoTag(tagNumber: Long) {
  override def toString = String.valueOf(tagNumber)
}

trait ProtoTagConversions {

  implicit def tag2long(tag: ProtoTag): Long = {
    tag.tagNumber
  }

  implicit def int2tag(tagNumber: Int): ProtoTag = {
    val longTag = tagNumber.toLong

    checkProtoTagNumber(longTag)
    ProtoTag(longTag)
  }

  implicit def long2tag(tagNumber: Long): ProtoTag = {
    checkProtoTagNumber(tagNumber)

    ProtoTag(tagNumber)
  }

  implicit def str2tag(tagString: String): ProtoTag = {
    long2tag(tagString.toLong)
  }

  implicit def any2tag(anyValue: Any): ProtoTag = {
    str2tag(anyValue.toString)
  }

  /**
   * Check the tag number is valid.
   * Please note that values between 1900 and 1999 are reserved for internal use, and can not be used.
   */
  def checkProtoTagNumber(tagNumber: Long) {
    if(!(tagNumber > 0 && tagNumber <= 536870911 && (tagNumber < 1900 || tagNumber > 1999))){
      throw new RuntimeException("Invalid tag: " + tagNumber)
    }
  }
}
