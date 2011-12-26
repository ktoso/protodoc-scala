package pl.project13.protodoc.templating

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import Mustache._

class MustacheFilenameTest
  extends FunSuite
  with ShouldMatchers {

  test("Should create mustache template filenames") {
    // given
    val template = "template"

    // when
    val mustache = template.mustache

    // then
    mustache should equal(Mustache.TemplatesDirPath + "/templates/" + template + ".mustache")
  }

}