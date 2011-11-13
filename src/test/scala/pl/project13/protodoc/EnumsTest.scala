package pl.project13.protodoc

import exceptions.{ProtoDocVerificationException, UnknownTypeException}
import model._
import model.IntProtoMessageField._
import model.StringProtoMessageField._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class EnumsTest extends FlatSpec with ShouldMatchers
                                 with ProtoTagConversions {

  "Enum" should "be parseable inside of an Message" in {
    val msg = ProtoBufCompiler.compileOne("""
    message Wiadomosc {
      required string some_field = 1;
      required string some_field_zomg = 5;

      enum MyEnum {
        VALUE = 2;
        OTHER = 4;
        LAST = 5;
      }
    }""").asInstanceOf[ProtoMessageType]

    msg.fields should have size (2)
    msg.enums should have size (1)

    val values = ProtoEnumValue("VALUE", 2) :: ProtoEnumValue("OTHER", 4) :: ProtoEnumValue("LAST", 5) :: Nil
    val expected = ProtoEnumType("MyEnum", ".Wiadomosc", values)
    msg.enums should contain (expected)
  }

  it should "be usable as field type" in {
    val msg = ProtoBufCompiler compileOne """
    message WiadomoscDwaPola {

      enum EnumType {
        EMAIL = 1;
        SMS = 2;
      }

      required EnumType theEnum = 2;
//      optional string pole = 1;
//      ^^^^^^^^^^^^^^^^^^^^^^^^^ it's commented out
    }"""

    msg.fields should have size (1)
    msg.enums should have size (1)

    val enumType: ProtoEnumType = msg.enums.head
    val enumField: ProtoMessageField = msg.fields.last // todo head and last are NOT really deterministic here!
    enumField should have (
      'fieldName ("theEnum"),
      'scalaTypeName (enumType.typeName),
      'protoTypeName (enumType.typeName),
      'tag (ProtoTag(2)),
      'modifier (RequiredProtoModifier()),
      'defaultValue (null)
    )
  }

  it should "be usable even before it's type declaration" in {
    val result = ProtoBufCompiler compileOne """
    message Msg {
      required SomeSecondEnum field = 1;
  
      enum SomeSecondEnum {
        SMS = 1;
      }
    }
    """

    result.fields should have size (1)
    result.enums should have size (1)

    val enumType: ProtoEnumType = result.enums.head
    val enumField: ProtoMessageField = result.fields.last
    enumField should have (
      'fieldName ("field"),
      'scalaTypeName (enumType.typeName),
      'protoTypeName (enumType.typeName),
      'tag (ProtoTag(1)),
      'modifier (RequiredProtoModifier()),
      'defaultValue (null)
    )
  }

  "Undefined enum" should "be type checked, so an not existing enum type used as field type will fail compiling" in {
    evaluating { 
      ProtoBufCompiler compile """
        message Msg {
        required NFJHSFEnum field = 1;
      }
      """
    } should produce [ProtoDocVerificationException]
  }
}