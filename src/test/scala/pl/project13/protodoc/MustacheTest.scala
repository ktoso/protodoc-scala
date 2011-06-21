package pl.project13.protodoc

import model._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.fusesource.scalate.TemplateEngine
import templating.ProtoDocTemplateEngine

/**
*
* @author Konrad Malawski
*/
class MustacheTest extends FlatSpec with ShouldMatchers {

  val templateEngine = new ProtoDocTemplateEngine()

  "ProtoDocTemplateEngine" should "render table of contents from sample data" in {
    val page = templateEngine.renderTableOfContents(List())

    page should include ("Table of Contents")
  }

}