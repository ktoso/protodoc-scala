package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 *
 * @author Konrad Malawski
 */
class InnerMessagesTest extends FlatSpec with ProtoTagConversions
                                         with ShouldMatchers {

  "Inner message" should "be parsed properly" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    message MyMessage {
      message InnerMessage {
        required int32 number = 1;
      }
    }
    """)

    val intField = IntProtoMessageField("number", "int32", 1, RequiredProtoModifier())
    val inner = ProtoMessage("InnerMessage", ".MyMessage", fields = List(intField))
    result should equal (ProtoMessage("MyMessage", innerMessages = List(inner)))
  }

}