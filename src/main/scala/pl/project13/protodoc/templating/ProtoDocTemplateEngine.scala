package pl.project13.protodoc.templating

import org.fusesource.scalate._
import layout.DefaultLayoutStrategy
import pl.project13.protodoc.model.ProtoMessage
import java.io.File

/**
 *
 * @author Konrad Malawski
 */
class ProtoDocTemplateEngine {

  val rootDir = new File("/home/ktoso/coding/protodoc-scala/src/main/templates/")

  val engine = new TemplateEngine(List(rootDir))
  engine.layoutStrategy = new DefaultLayoutStrategy(engine, "mylayout.mustache")

  // default bindings
  engine.bindings = List(Binding(name = "protodoc_version",
                                 className = "String",
                                 defaultValue = Option(""""v1.0"""")))

  def renderTableOfContents(contents: List[ProtoMessage]) = {
    val data = Map("contents" -> contents,
                   "test" -> "it's working, yay!")

    engine.layout("index.mustache", data)
  }

  def renderMessagePage() = {
    "" // todo implement me
  }

}