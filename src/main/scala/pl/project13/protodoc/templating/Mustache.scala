package pl.project13.protodoc
package templating

private[templating] object Mustache {
  val TemplatesDirPath = "/tmp/mustache-tmp"
  val Suffix = ".mustache"

  implicit def str2mustache(filename: String): MustacheFilename = new MustacheFilename(filename)
}

private[templating] class MustacheFilename(val filename: String) {
  import Mustache._

  def mustache = TemplatesDirPath + "/templates/" + filename + Suffix
}
