package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class PackageTest extends FlatSpec with ProtoTagConversions
                                   with ShouldMatchers {

  "Package name" should "be read from proto file with it" in {
    val result: ProtoMessageType = ProtoBufParser.parse("""
    package pl.project13;

    message Wiadomosc {
    }
    """)

    result.packageName should equal ("pl.project13")
  }

  "InnerInnerMsg package" should "contain it's super Messages in package name" in {
    val msg: ProtoMessageType = ProtoBufParser.parse("""
    package pl.project13;

    message Msg {
      message InnerMsg {
        message InnerInnerMsg {
          message InnerInnerInnerMsg {
            enum QuadrupleInnerEnum {}
          }
        }
      }
    }
    """)

    msg.packageName should equal ("pl.project13")

    val innerMsg = msg.innerMessages.head
    innerMsg.packageName should equal ("pl.project13.Msg")

    val innerInnerMsg = innerMsg.innerMessages.head
    innerInnerMsg.packageName should equal ("pl.project13.Msg.InnerMsg")

    val innerInnerInnerMsg = innerInnerMsg.innerMessages.head
    innerInnerInnerMsg.packageName should equal ("pl.project13.Msg.InnerMsg.InnerInnerMsg")

    val quadrupleInnerEnum = innerInnerInnerMsg.enums.head
    quadrupleInnerEnum.packageName should equal  ("pl.project13.Msg.InnerMsg.InnerInnerMsg.InnerInnerInnerMsg")
  }

}