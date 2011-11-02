package pl.project13.protodoc

import model._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{GivenWhenThen, FlatSpec}

class TagVerifierTest extends FlatSpec
                         with GivenWhenThen
                         with ProtoTagConversions
                         with ShouldMatchers {
  
  "validateTags" should "detect duplicated tags" in {
    given("an message with duplicated field tags")
    val fields = List(IntProtoMessageField("first", "int32", 1, RequiredProtoModifier()),
                      IntProtoMessageField("second", "int32", 2, RequiredProtoModifier()),
                      IntProtoMessageField("fail", "int32", 2, RequiredProtoModifier()),
                      IntProtoMessageField("secondToLast", "int32", 3, RequiredProtoModifier()),
                      IntProtoMessageField("last", "int32", 4, RequiredProtoModifier()))
    val tags = fields.map(_.tag)

    val context = ProtoMessageType("Sample", "pl.project13", fields)

    when("tags are validated")
    val uniquenessErrors = TagVerifier.validateTags(context, tags)
    
    then("it should detect duplicates")
    uniquenessErrors.length should equal (1)

    and("errors are about the 'second' and 'fail' fields")
    val aboutFields: String = uniquenessErrors.head.about
    aboutFields should include("second")
    aboutFields should include("fail")
  }

  "validateTags" should "should 'OK' a valid tags list" in {
      given("a valid message")
      val context = ProtoMessageType("Sample", "pl.project13", List())
      val tags: List[ProtoTag]= List(1, 2, 3)

      when("tags are validated")
      val uniquenessErrors = TagVerifier.validateTags(context, tags)

      then("it have not detected any problems")
      uniquenessErrors.length should equal (0)
    }
}