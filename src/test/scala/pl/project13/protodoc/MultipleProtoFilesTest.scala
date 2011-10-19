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
    required int32 aNumber = 1;
  }
  """
  val SECOND_INDEPENDENT_FILE = """
    package pl.project13.test;

    message IAmIndependent {
      optional string surname = 1;
    }
    """

  behavior of  "Parser given multiple files"
  
  it should "parse multiple seperate (independent) files" in {
    val protos = INDEPENDENT_FILE :: SECOND_INDEPENDENT_FILE :: Nil
    
    val parsedProtos = ProtoBufParser.parse(protos)

    parsedProtos should have length (2)
  }
}