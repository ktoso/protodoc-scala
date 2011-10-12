package pl.project13.protodoc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import model._

/**
 * @author Konrad Malawski
 */
class MultipleProtoFilesTest extends FlatSpec with ProtoTagConversions
                                              with ShouldMatchers {

  val INDEPENDENT_FILE = """
  package pl.project13.test;

  message SomeMessage {
    required int aNumber = 1;
  }
  """
  val SECOND_INDEPENDENT_FILE = """
    package pl.project13.test;

    message IAmIndependent {
      optional string surname = 1;
    }
    """


  describe("Parsing for multiple files") {
      "Parser" should {
          "parse multiple seperate (independent) files" in {
              ProtoBufParser.parse()
          }
      }
  }

}