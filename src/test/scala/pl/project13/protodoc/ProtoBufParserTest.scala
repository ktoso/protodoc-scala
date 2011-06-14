package pl.project13.protodoc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class ProtoBufParserTest extends FlatSpec with ShouldMatchers {

  "Parser" should "parse single simple message" in {
    val result: Any = ProtoBufParser.parse("""
    message Wiadomosc {
      string name = 1;
    }
    """)

    result

    println(result)
  }

  "Parser" should "parse single message with enum" in {
    val result: Any = ProtoBufParser.parse("""
    message Wiadomosc {
      string name = 1;

      enum MyEnum {
        EMAIL = 1;
        SMS = 2;
      }
    }
    """)

    result

    println(result)
  }
}