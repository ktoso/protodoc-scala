package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class ProtoBufParserTest extends FlatSpec with ShouldMatchers
                                          with ProtoTagConversions {

  ProtoBufParser.verbose = true

  "Parser" should "parse single simple message" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string name = 1;
    }
    """)

    result.fields.head should have (
      'tag (ProtoTag(1)),
      'fieldName ("name"),
      'protoTypeName ("string"),
      'scalaTypeName ("java.lang.String"),
      'modifier (RequiredProtoModifier())
    )
  }

  "Parser" should "parse single message with enum" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string name = 1;

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
      optional int32 one = 1;
      optional int32 two = 2 [default = 42];
      required int32 three = 3;
      required int32 four = 4 [default = 42];
      repeated bool five = 5;
    }""")

    result

    // then
    result.fields should have length (5)

    val fieldNames = result.fields.map(_.fieldName)
    fieldNames should contain ("one")
    fieldNames should contain ("two")
    fieldNames should contain ("three")
    fieldNames should contain ("four")
    fieldNames should contain ("five")
  }

  "addOuterMessageInfo" should "fix package info of inner enums/msgs" in {
    val data = List(ProtoMessage("InnerMessage", "", innerMessages = ProtoMessage("InnerInnerMessage", "") :: Nil))

    println("Will fix packages of: " + data)

    val fixedData = ProtoBufParser.addOuterMessageInfo("Outer", "pl.project13", data)

    println("Fix resulted in: " + fixedData)

    val fixedMessage = fixedData.head.asInstanceOf[ProtoMessage]
    fixedMessage should have (
      'packageName ("pl.project13.Outer")
    )
    fixedMessage.innerMessages.head should have (
      'packageName ("pl.project13.Outer.InnerMessage")
    )
  }

}