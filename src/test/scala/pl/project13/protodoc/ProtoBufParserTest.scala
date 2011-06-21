package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class ProtoBufParserTest extends FlatSpec with ShouldMatchers
                                          with HasProtoTag {

  "Parser" should "parse single simple message" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      string name = 1;
    }
    """)

    result
  }

  "Parser" should "parse single message with enum" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      string name = 1;

      enum MyEnum {
        EMAIL = 1;
        SMS = 2;
      }
    }
    """)

    result.enums should have size (1)

    val myEnum = result.enums.head
    myEnum.values should have size (2)
    myEnum.values should contain (ProtoEnumValue("EMAIL", ProtoTag(1)))
    myEnum.values should contain (ProtoEnumValue("SMS", ProtoTag(2)))
  }

  "Parser" should "have no problems with field modifiers" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {
      optional int one = 1;
      optional int two = 2 [default = 42];
      required int three = 3;
      required int four = 4 [default = 42];
      repeated bool five = 5;
    }""")

    result

    // then
    result.fields should have length (4)

    val fieldNames = result.fields.map(_.fieldName)
    fieldNames should contain ("one")
    fieldNames should contain ("two")
    fieldNames should contain ("three")
    fieldNames should contain ("four")
  }

  "Default string value" should "have proper value" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      optional string wiad = 12 [default = "loremipsum"];
    }
    """)

    // todo validate the value
  }

}