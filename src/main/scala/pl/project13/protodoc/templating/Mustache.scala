package pl.project13.protodoc
package templating

private[templating] class MustacheFilename(val filename: String) {
  import Mustache._

  def mustache = TemplatesDirPath + "/" + filename + Suffix
}

private[templating] object Mustache {
  val TemplatesDirPath = "/home/ktoso/code/protodoc-scala/src/main/templates"
  val Suffix = ".mustache"

  implicit def str2mustache(filename: String): MustacheFilename = new MustacheFilename(filename)
}