package pl.project13.protodoc.templating

import org.fusesource.scalate._
import pl.project13.protodoc.model.ProtoMessage

/**
 *
 * @author Konrad Malawski
 */
class ProtoDocTemplateEngine {

  val engine = new TemplateEngine
  engine.bindings = List(Binding("protodoc_version", "v1.0"))

  def renderTableOfContents(contents: List[ProtoMessage]) = {
    engine.layout("toc.ssp")
  }

  def renderMessagePage() = {
    "" // todo implement me
  }

}