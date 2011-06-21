package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class FieldsTest extends FlatSpec with HasProtoTag
                                  with ShouldMatchers {

  "Parser" should "parse single int field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required int number = 1;
    }
    """)

    result

    result.fields.head should equal (IntProtoMessageField("number", 1, RequiredProtoModifier()))
  }

  "Parser" should "parse single int32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required int32 number = 1;
    }
    """)

    result

    result.fields.head should equal (IntProtoMessageField("number", 1, RequiredProtoModifier()))
  }

  "Parser" should "parse single fixed32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required fixed32 number = 1;
    }
    """)

    result

    result.fields.head should equal (IntProtoMessageField("number", 1, RequiredProtoModifier()))
  }

  "Parser" should "parse single sfixed32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required sfixed32 number = 1;
    }
    """)

    result

    result.fields.head should equal (IntProtoMessageField("number", 1, RequiredProtoModifier()))
  }

  "Parser" should "parse single int64 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required int64 number = 1;
    }
    """)

    result

    result.fields.head should equal (LongProtoMessageField("number", 1, RequiredProtoModifier()))
  }

  "Parser" should "parse single fixed64 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      repeated fixed64 number = 1;
    }
    """)

    result

    result.fields.head should equal (LongProtoMessageField("number", 1, RepeatedProtoModifier()))
  }

  "Parser" should "parse single optional string field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      optional string name = 1;
    }
    """)

    result

    result.fields.head should equal (StringProtoMessageField("name", 1, OptionalProtoModifier()))
  }

  "Parser" should "parse single required string field with default value" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string name = 1 [default = "loremipsum"]; // thats ok, says the spec
    }
    """)

    result

    result.fields.head should equal (StringProtoMessageField("name", 1, RequiredProtoModifier(), "loremipsum"))
  }

}