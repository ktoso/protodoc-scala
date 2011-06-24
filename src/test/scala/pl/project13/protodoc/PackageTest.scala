package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
*
* @author Konrad Malawski
*/
class PackageTest extends FlatSpec with ProtoTagConversions
                                   with ShouldMatchers {

  "Package name" should "be read from proto file with it" in {
    val result: ProtoMessage = ProtoBufParser.parse("""
    package pl.project13;

    message Wiadomosc {
    }
    """)

    result.packageName should equal ("pl.project13")
  }

}