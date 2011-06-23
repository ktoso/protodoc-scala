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
class EnumsTest extends FlatSpec with ShouldMatchers {

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
  }

  "Message with 2 fields" should "in fact have 2 fields" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {
      required string pole = 23;
      required int32 last = 42 [default = 42];
    }""")

    result

    // then
    result.fields should have length (2)

    result.fields.map(_.fieldName) should contain ("pole")
    result.fields.map(_.fieldName) should contain ("last")
  }

  "Enum" should "be usable as field type" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {

      enum EnumType {
        EMAIL = 1;
        SMS = 2;
      }

      optional string pole = 1;
      required EnumType theEnum = 2;
    }""")

    result

    // then
    result.fields should have length (2)

    result.fields.map(_.fieldName) should contain ("pole")
    result.fields.map(_.fieldName) should contain ("last")
  }
}