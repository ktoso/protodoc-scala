package pl.project13.protodoc

import model._
import model.IntProtoMessageField._
import model.StringProtoMessageField._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class OutEnumRepresentationTest extends FlatSpec with ShouldMatchers {

  "Parser" should "generate valid ProtoEnumTypeField" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      enum MyEnum {
        VALUE = 2;
        OTHER = 4;
        LAST = 5;
      }
    }""")

    result

    Console.println(result)
  }

  "Three field message" should "in fact have 2 fields" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {
      string field = 23;
      int last = 42 [default = 42];
    }""")

    result

    Console.println(result)

    // then
    result.fields should have length (2)

    result.fields.map(_.fieldName) should contain ("field")
    result.fields.map(_.fieldName) should contain ("last")
  }
}