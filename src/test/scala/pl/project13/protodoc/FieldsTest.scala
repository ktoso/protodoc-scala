package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class FieldsTest extends FlatSpec with HasProtoTag with ShouldMatchers {

  "Parser" should "parse single int field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      int number = 1;
    }
    """)

    result

    Console.println(result)

    result.fields.head should equal (IntProtoMessageField("number", 1, NoProtoModifier()))
  }

  "Parser" should "parse single required string field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string name = 1;
    }
    """)

    result

    Console.println(result)

    result.fields.head should equal (StringProtoMessageField("name", 1, RequiredProtoModifier()))
  }

}