package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import pl.project13.protodoc.exceptions.UnknownTypeException

/**
*
* @author Konrad Malawski
*/
class CommentsTest extends FlatSpec with ShouldMatchers
                                    with ProtoTagConversions {

  "Comment on top level message" should "be parsed properly" in {
    val message = ProtoBufParser.parseOne("""/** This is a test comment 000 */
    message HasDocumentationOnIt {}""")

    message.comment should include ("This is a test comment 000")
  }

  "Comment on field" should "be parsed properly" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
      /* This is my comment 111 */ optional string name = 1;
    }""")

    val field = message.fields.head
    field.comment should include ("This is my comment 111")
    field.tag should equal (ProtoTag(1))
    field.fieldName should equal ("name")
  }

  it should "be parsed properly, even if inline" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
      /* This is my comment 333 */
      optional string name = 1;
    }""")

    val field = message.fields.head
    field.comment should include ("This is my comment")
    field.tag should equal (ProtoTag(1))
    field.fieldName should equal ("name")
  }

  it should "be parsed properly, using JavaDoc style markers" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
      /** This is my comment 444 */
      optional string name = 1;
    }""")

    val field = message.fields.head
    field.comment should equal (" This is my comment 444 ")
    field.tag should equal (ProtoTag(1))
    field.fieldName should equal ("name")
  }

  it should "be parsed properly, even if spanning multiple lines" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
      /**
       * This is my comment
       * Second comment line 555
       */
       optional string name = 1;
    }""")

    val field = message.fields.head
    field.comment should include ("This is my comment\nSecond comment line")
    field.tag should equal (ProtoTag(1))
    field.fieldName should equal ("name")
  }

  "Multi Line Comment on top level message" should "be parsed properly" in {
    val message = ProtoBufParser.parseOne("""
    /**
     * This is a test comment 666
     */
    message HasDocumentedEnum {}
    """)

    message.comment should include ("This is a test comment")
  }

  "Multi Line Comment on inner enum" should "be parsed properly" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
      /**
       * This is my comment
       * Second comment line 777
       */
       enum SomeEnum {
         /** Comment on an enum value */
         EMAIL = 1;
       }
    }""")

    val enum = message.enums.head
    enum.comment should include ("This is my comment\nSecond comment line")
    enum.typeName should equal ("SomeEnum")

    enum.values should have size (1)
    val EMAIL = enum.values.head
    EMAIL.valueName should equal ("EMAIL")
    EMAIL.tag should equal (ProtoTag(1))
    EMAIL.comment should include ("Comment on an enum value")
  }

  "Comment on enum value" should "be parsed properly" in {
    val message = ProtoBufParser.parseOne("""
    message HasDocumentedEnum {
       enum SomeEnum {
         /** Comment on an enum value 888 */
         EMAIL = 1;
       }
    }""")

    message.messageName should equal ("HasDocumentedEnum")
    
    val enum = message.enums.head
    enum.values should have size (1)

    val EMAIL = enum.values.head
    EMAIL.valueName should equal ("EMAIL")
    EMAIL.tag should equal (ProtoTag(1))
    EMAIL.comment should include ("Comment on an enum value")
  }
}