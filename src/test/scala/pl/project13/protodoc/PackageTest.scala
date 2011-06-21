package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class PackageTest extends FlatSpec with HasProtoTag
                                   with ShouldMatchers {

  "Package name" should "be read from proto file with it" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    package pl.project13;

    message Wiadomosc {
      required int32 number = 1;
    }
    """)

    val expected = IntProtoMessageField("number", "int32", 1, RequiredProtoModifier())
    result.fields.head should equal (expected)
  }

}