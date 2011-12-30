package pl.project13.protodoc

import model._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{GivenWhenThen, Spec, FlatSpec}

/**
 *
 * @author Konrad Malawski
 */
class DeprecationTest extends Spec
  with GivenWhenThen
  with ShouldMatchers {

  describe("Message with deprecations") {
    it("the deprecated fields should be detected") {
      val msg = ProtoBufCompiler.compileOne("""
        message MsgWith1Deprecation {
          /** @deprecated */
          required string depr_name = 45;
        }""").asInstanceOf[ProtoMessageType]

      val field = msg.fields.head
      field should be ('deprecated)
    }

    it("should not detect deprecations where there are none") {
      val msg = ProtoBufCompiler.compileOne("""
        message MsgWithNo {
          required string depr_name = 45;
        }""").asInstanceOf[ProtoMessageType]

      val field = msg.fields.head
      field should not be ('deprecated)
    }

    it("should detect deprecation on message type") {
      val msg = ProtoBufCompiler.compileOne("""
        /** @deprecated */
        message DeprecatedMsg {}""")

      msg should be ('deprecated)
    }

    it("should detect deprecation on enum type") {
      val msg = ProtoBufCompiler.compileOne("""
        /** @deprecated */
        enum DeprecatedEnumeration {}""")

      msg should be ('deprecated)
    }

    it("should detect inner types") {
      val msg = ProtoBufCompiler.compileOne("""
        message OkMsgWithInnerDeprecations {

          /** @deprecated */
          message DontUseMeMessage { }

          /** @deprecated */
          enum InnerDeprecatedEnum {
             SMS =    1;
          }
        }""").asInstanceOf[ProtoMessageType]

      msg should not be ('deprecated)
      msg.innerMessages.head should be ('deprecated)
      msg.enums.head should be ('deprecated)
    }

  }
}