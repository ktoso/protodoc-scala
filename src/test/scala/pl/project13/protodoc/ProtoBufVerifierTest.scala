package pl.project13.protodoc

import exceptions.ProtoDocVerificationException
import model._
import org.scalatest.matchers._
import org.scalatest._

/**
 * @author Konrad Malawski
 */
class ProtoBufVerifierTest extends FeatureSpec
                              with Assertions
                              with GivenWhenThen
                              with ShouldMatchers
                              with ProtoTagConversions
                              with Logger {
  
  feature("The Verifier should validate field types") {

    scenario("an unresolvable field should be detected") {
      given("a message with an invalid fieldtype")
      val msg = """
      message HasErrorField {
        required UnknownType errorField = 41;
      }"""

      when("the message is parsed and verified")
      val exception = intercept[ProtoDocVerificationException] {
        val messages = ProtoBufCompiler.compile(msg)
      }

      exception.verificationResult
      log(exception.verificationResult)

      then("the Verifier report it as invalid")
      exception.verificationResult.invalid should equal(true)
      exception.verificationResult.valid should equal(false)

      and("it should point out that the UnknownType is unresolvable")
      exception.verificationResult.errors should have length (1)
    }
  }
}