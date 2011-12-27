package pl.project13.protodoc

import model._
import org.scalatest.matchers.ShouldMatchers
import pl.project13.protodoc.exceptions.UnknownTypeException
import org.scalatest.{GivenWhenThen, Spec}

/**
 * @author Konrad Malawski
 */
class RealSimpleParsingTest
  extends Spec
  with GivenWhenThen
  with ShouldMatchers
  with ProtoTagConversions {

  val TestString =
    """|package pl.project13;
    |
    |/**
    | * This is a simple Message which has some Inner Message defined.
    | * Also, note that it has a default value on the name property.
    | */
    |message MessageWithInner {
    |    /**
    |     * A number is just a simple property
    |     */
    |    required int32 number = 1;
    |
    |    /**
    |     * This can be a name of your likeing, the default value is "lorem ipsum" etc
    |     */
    |    required string name = 2 [default = "loremipsum"];
    |
    |
    |    /**
    |     * Whoa, this is a comment on an inner message!
    |     * ProtoDoc is so cool!
    |     */
    |    message InnerMessage {
    |
    |        /**
    |         * This is a comment on an inner messages field, cool~<br/>
    |         * <br/>
    |         * You may use it like this in your generated Java code:
    |         * <pre><code> setName("StringMyName");</code></pre>
    |         */
    |        optional string name = 3;
    |    }
    |
    |   optional OuterEnumeration instanceOfOuterEnum = 3;
    |}
    |
    |enum OuterEnumeration {
    |   OUTSIDE = 1;
    |   INSIDE = 2;
    |}
    |""".stripMargin

  describe("Parsing of an real message, with outer enum") {

    ProtoBufParser.verbose = true

    it("should be parsed properly") {
      given("A real proto file")

      when("it is parsed")
      val result = ProtoBufParser.parse(TestString)

      and("it is verified")
      ProtoBufVerifier.verify(result)

      then("parsed size should be 2")
      result.size should equal(2)

      and("the inner message should be detected")
      val withInner = result.head
      withInner.fullName should equal("pl.project13.MessageWithInner")

      and("the inner message should be named properly")
      withInner.innerMessages.head.fullName should equal("pl.project13.MessageWithInner.InnerMessage")

      and("the enum field should have the proper type resolved")
      val instanceOfOuterEnum = withInner.fields.find(_.fieldName == "instanceOfOuterEnum")
      instanceOfOuterEnum should be ('defined)

      and("it's tag should be equal 3")
      instanceOfOuterEnum.get.tag should equal (ProtoTag(3))
      
      and("it's resolved type should be the outer enumeration")
      instanceOfOuterEnum.get.protoTypeName should equal ("pl.project13.OuterEnumeration")

      and("the outer enum should be parsed and named properly")
      val outerEnum = result(1)
      outerEnum.fullName should equal("pl.project13.OuterEnumeration")
    }
  }
}