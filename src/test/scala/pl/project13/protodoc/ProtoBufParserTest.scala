package pl.project13.protodoc

import model._
import org.scalatest.matchers._
import org.scalatest._

/**
 * @author Konrad Malawski
 */
class ProtoBufParserTest extends FeatureSpec
                            with GivenWhenThen
                            with ShouldMatchers
                            with ProtoTagConversions {
  
  feature("The Verifier should validate field types") {

    scenario("an unresolvable field should be detected") {
      given("a message with an invalid fieldtype")
      val msg = """
      message HasErrorField {
        required UnknownType errorField = 41;
      }"""

      when("the message is parsed")
      ProtoBufCompiler.parseNoVerify(msg)

      and("the message is then verivied")
      then("the Verifier should detect the unresolvable field")
      and("the verification should return false")
    }
  }
}