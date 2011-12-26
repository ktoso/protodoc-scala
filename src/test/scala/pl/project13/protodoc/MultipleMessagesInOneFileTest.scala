package pl.project13.protodoc

import model._
import org.scalatest.matchers._
import org.scalatest._

/**
 * @author Konrad Malawski
 */
class MultipleMessagesInOneFileTest extends Spec
                                with Assertions
                                with GivenWhenThen
                                with ShouldMatchers
                                with ProtoTagConversions
                                with Logger {

  val defined = 'defined

  describe("Parser") {

    it("should deal with multiple messages defined in the root level of one file") {
      given("a proto file with two root devel messages")
      val msg = """
      message MisticTypeBefore {
        required int32 it = 23;
      }

      message HasResolvableField {
        required MisticTypeBefore theField = 41;
      }"""

      when("the messages are parsed and verified")
      val protos = ProtoBufCompiler compile msg

      then("the result should contain two messages")
      protos should have length (2)
      protos map(_.fullName) should (contain ("MisticTypeBefore") and contain ("HasResolvableField"))
    }

    it("should deal with multiple enums and messages defined in root scope, in one file") {
      given("a proto file with two root devel messages")
            val msg = """
            message MisticTypeBefore {
              required int32 it = 23;
            }

            message HasResolvableField {
              required MisticTypeBefore theField = 41;
            }

            enum WeirdEnum {
              VALUE = 213;
              SECOND = 34;
            }
            """

            when("the messages and enum are parsed and verified")
            val protos = ProtoBufCompiler compile msg

            then("the result should contain two messages and one enum")
            protos should have length (3)
            protos map(_.fullName) should (
              contain ("MisticTypeBefore")
              and contain ("HasResolvableField")
              and contain ("WeirdEnum")
            )
    }
  }
}