package pl.project13.protodoc.templating

import org.fusesource.scalate._
import layout.DefaultLayoutStrategy
import java.io.File
import pl.project13.protodoc.model.ProtoMessage

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

  /**
   * Renders the HTML with all Messages, a table of contents so to say
   */
  def renderTableOfContents(contents: List[ProtoMessage]) = {
    val sortedContents = contents.sortBy(m => m.messageName)
    val data = Map("contents" -> sortedContents)

    engine.layout("index.mustache", data)
  }

  def renderMessagePage(msg: ProtoMessage) = {
    val data = Map("messageName" -> msg.messageName,
                   "packageName" -> msg.packageName,
                   "fields" -> msg.fields,
                   "enums" -> msg.enums,
                   "innerMessages" -> msg.innerMessages)

    engine.layout("message.mustache", data)
  }

}