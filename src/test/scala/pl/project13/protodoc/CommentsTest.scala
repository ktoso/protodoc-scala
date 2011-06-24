package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import pl.project13.protodoc.exceptions.UnknownTypeException

/**
*
* @author Konrad Malawski
*/
class CommentsTest extends FlatSpec with ShouldMatchers
                                 with ProtoTagConversions {

  ProtoBufParser.verbose = true

  "Enum" should "be parseable inside of an Message" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message Wiadomosc {
      required string some_field = 1;
      required string some_field_zomg = 5;

      enum MyEnum {
        VALUE = 2;
        OTHER = 4;
        LAST = 5;
      }
    }""")

    result.fields should have size (2)
    result.enums should have size (1)

    val values = ProtoEnumValue("VALUE", 2) :: ProtoEnumValue("OTHER", 4) :: ProtoEnumValue("LAST", 5) :: Nil
    val expected = ProtoEnumType("MyEnum", "Wiadomosc", values)
    result.enums should contain (expected)
  }

  "Enum" should "be usable as field type" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message WiadomoscDwaPola {

      enum EnumType {
        EMAIL = 1;
        SMS = 2;
      }

      required EnumType theEnum = 2;
      optional string pole = 1;
    }""")

    result.fields should have size (2)
    result.enums should have size (1)

    val enumType: ProtoEnumType = result.enums.head
    val enumField: ProtoMessageField = result.fields.head
    enumField should have (
      'tag (ProtoTag(2)),
      'modifier (RequiredProtoModifier()),
      'fieldName ("theEnum"),
      'defaultValue (null),
      'scalaTypeName (enumType.typeName),
      'protoTypeName (enumType.typeName)
    )
  }

  "Enum" should "stick to 'known' enums, and not allow random field types" in {
    evaluating {
      ProtoBufParser.parse("""
        message Msg {
        enum SomeEnum {
          SMS = 1;
        }

        required UndefinedEnum field = 1;
      }
      """)
    } should produce [UnknownTypeException]
  }
}