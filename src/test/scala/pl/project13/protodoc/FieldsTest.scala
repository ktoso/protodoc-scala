package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 *
 * @author Konrad Malawski
 */
class FieldsTest extends FlatSpec with ProtoTagConversions
                                  with ShouldMatchers {

  "Message with 2 fields" should "in fact have 2 fields" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {
      required string pole = 23;
      required int32 last = 42 [default = 42];
    }""")

    result

    // then
    result.fields should have length (2)

    result.fields.map(_.fieldName) should contain("pole")
    result.fields.map(_.fieldName) should contain("last")
  }

  "Parser" should "parse single int32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required int32 number = 1;
    }
    """)

    val expected = IntProtoMessageField("number", "int32", 1, RequiredProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single fixed32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required fixed32 number = 1;
    }
    """)

    val expected = IntProtoMessageField("number", "fixed32", 1, RequiredProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single sfixed64 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required sfixed64 number = 1;
    }
    """)

    val expected = LongProtoMessageField("number", "sfixed64", 1, RequiredProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single int64 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required int64 number = 1;
    }
    """)

    val expected = LongProtoMessageField("number", "int64", 1, RequiredProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single fixed64 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      repeated fixed64 number = 1;
    }
    """)

    val expected = LongProtoMessageField("number", "fixed64", 1, RepeatedProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single optional string field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      optional string name = 1;
    }
    """)

    val expected = StringProtoMessageField("name", 1, OptionalProtoModifier())
    result.fields.head should equal(expected)
  }

  it should "parse single required string field with default value" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string name = 1 [default = "loremipsum"]; // thats ok, says the spec
    }
    """)

    val expected = StringProtoMessageField("name", 1, RequiredProtoModifier(), "loremipsum")
    result.fields.head should equal(expected)
  }

}