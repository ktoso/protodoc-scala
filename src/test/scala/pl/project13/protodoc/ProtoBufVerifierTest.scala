package pl.project13.protodoc

import exceptions.ProtoDocVerificationException
import model._
import org.scalatest.matchers._
import org.scalatest._

/**
 * @author Konrad Malawski
 */
class ProtoBufVerifierTest extends Spec
                                with Assertions
                                with GivenWhenThen
                                with ShouldMatchers
                                with ProtoTagConversions
                                with Logger {

  val defined = 'defined

  describe("The Verifier should validate field types") {

    it("should detect an unresolvable field") {
      given("a message with an invalid fieldtype")
      val msg = """
      message HasErrorField {
        required UnknownType errorField = 41;
      }"""

      when("the message is parsed and verified")
      val exception = intercept[ProtoDocVerificationException] {
        ProtoBufCompiler.compile(msg)
      }

      exception.verificationResult
      log(exception.verificationResult)

      then("the Verifier report it as invalid")
      exception.verificationResult.invalid should equal(true)
      exception.verificationResult.valid should equal(false)

      and("it should point out that the UnknownType is unresolvable")
      exception.verificationResult.errors should have length (1)

      val err = exception.verificationResult.errors.head
      err.about should equal ("errorField")
    }

    it("should have no problems with resolvable field Type") {
      given("a message with valid, resolvable fieldtype, defined before the message")
      val msg = """
      message MisticTypeBefore {
        required int32 it = 23;
      }

      message HasResolvableField {
        required MisticTypeBefore errorField = 41;
      }"""

      when("the message is parsed and verified")
      val protos = ProtoBufCompiler.compile(msg)

      then("the result should have a resolved HasResolvableField message")
      val hasResolvableField = protos find (_.messageName == "HasResolveableField")

      hasResolvableField should be (defined)
    }
  }
}