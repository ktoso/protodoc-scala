package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class InnerMessagesTest extends FlatSpec with HasProtoTag
                                  with ShouldMatchers {

  "Parser" should "parse single int32 field" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message MyMessage {
      message InnerMessage {
        required int32 number = 1;
      }
    }
    """)

    val intField = IntProtoMessageField("number", "int32", 1, RequiredProtoModifier())
    val inner = ProtoMessage("InnerMessage", fields = List(intField))
    val expected = ProtoMessage("MyMessage", innerMessages = List(inner))

    result.fields.head should equal (expected)
  }

}