package pl.project13.protodoc.templating

import _root_.pl.project13.protodoc.model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author Konrad Malawski
 */
class TableOfContentsTest extends FlatSpec with ShouldMatchers {

  val templateEngine = new ProtoDocTemplateEngine()

  val sampleProtoMessage = List(ProtoMessage("MyMessage", "pl.project13"),
                                ProtoMessage("SecondMessage", "pl.project13"),
                                ProtoMessage("Person", "pl.project13"),
                                ProtoMessage("PhoneNumber", "pl.project13"),
                                ProtoMessage("Address", "pl.project13"),
                                ProtoMessage("PersonTypeEnum", "pl.project13"))

  "ProtoDocTemplateEngine" should "render table of contents from sample data" in {
    val page = templateEngine.renderTableOfContents(sampleProtoMessage)

    printToFile(new java.io.File("/tmp/test.html")) ({ p => page.foreach(p.print) })

    page should include ("Table of Contents")
    page should include ("MyMessage")
    page should include ("SecondMessage")
    page should include ("Person")
    page should include ("PhoneNumber")
    page should include ("Address")
    page should include ("PersonTypeEnum")
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Any) = {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

}