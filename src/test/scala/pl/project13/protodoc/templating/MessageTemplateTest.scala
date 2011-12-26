package pl.project13.protodoc.templating

import _root_.pl.project13.protodoc.ProtoBufParser
import _root_.pl.project13.protodoc.model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

/**
 * @author Konrad Malawski
 */
class MessageTemplateTest
  extends FlatSpec
  with ShouldMatchers
  with PrintToFile {

  val templateEngine = new ProtoDocTemplateEngine

  "ProtoDocTemplateEngine" should "render simple message page" in {
    val message = ProtoBufParser.parseOne("""
    package pl.project13.protobuf;

    message AmazingMessage {
     optional string name = 1;
     optional uint32 age = 2;

     enum Enumeration {
       IT = 54;
     }
    }
    """).asInstanceOf[ProtoMessageType] // we're sure of that here

    val page = templateEngine.renderTypePage(message)

    // debug print
    printToFile(new File("/tmp/message.html")) { p => page.foreach(p.print(_)) }

    page should include ("pl.project13.protobuf")
    page should include ("AmazingMessage")
    page should include ("pl.project13.protobuf.AmazingMessage")
    page should include ("name")
    page should include ("age")
  }

//  it should "render top level message comment" in {
//    val message = ProtoBufParser.parse("""
//    package pl.project13.protobuf;
//
//    /** I'm a protodoc comment */
//    message AmazingMessage {
//     optional string name = 1;
//     optional uint32 age = 2;
//    }
//    """).head
//
//    val page = templateEngine.renderTypePage(message)
//
//    page should include ("pl.project13.protobuf")
//    page should include ("AmazingMessage")
//    page should include ("name")
//    page should include ("age")
//    page should include ("I'm a protodoc comment")
//  }
}