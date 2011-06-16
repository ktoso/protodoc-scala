package pl.project13.protodoc

import model.{ProtoEnumValue, ProtoEnumTypeField, ProtoMessage}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class ProtoBufParserTest extends FlatSpec with ShouldMatchers {

  "Parser" should "parse single simple message" in {
    val result: Any = ProtoBufParser.parse("""
    message Wiadomosc {
      string name = 1;
    }
    """)

    result

    Console.println(result)
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

    result

    result.enums should have size (1)
    result.enums.head.values should have size (2)
    result.enums.head.values should contain (ProtoEnumValue("EMAIL"))
    result.enums.head.values should contain (ProtoEnumValue("SMS"))

    Console.println(result)
  }

  "Parser" should "have no problems with field modifiers" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {
      optional int one = 1;
      required int two = 2;
      required int three = 3 [default = 42];
      repeated bool four = 4;
    }""")

    result

    Console.println(result)

    // then
    result.fields should have length (4)

    val fieldNames = result.fields.map(_.fieldName)
    fieldNames should contain ("one")
    fieldNames should contain ("two")
    fieldNames should contain ("three")
    fieldNames should contain ("four")
  }
}