package pl.project13.protodoc.templating

import org.fusesource.scalate._
import layout.DefaultLayoutStrategy
import pl.project13.protodoc.model.ProtoMessage
import java.io.{FileWriter, File}
/**
 *
 * @author Konrad Malawski
 */
class ProtoDocTemplateEngine {

  var templatesDir = new File("/home/ktoso/coding/protodoc-scala/src/main/templates/")

  val engine = new TemplateEngine(List(templatesDir))
  engine.layoutStrategy = new DefaultLayoutStrategy(engine, "mylayout.mustache")

  // default bindings
  engine.bindings = List(Binding(name = "protodoc_version",
                                 className = "String",
                                 defaultValue = Option(""""v1.0"""")))

  /**
   * Renders the HTML with all Messages, a table of contents so to say
   */
  def renderTableOfContents(contents: List[ProtoMessage], outDir: String) = {
    var all = allInnerMessagesOf(contents).sortBy(m => m.messageName)

    Console.println("ALL MESSAGES: " + all.map(_.messageName))

    val data = Map("contents" -> all)

    val html = engine.layout("index.mustache", data)
    val filename: String = outDir + "/index.html"
    writeToFile(filename, html)
  }

  def renderMessagePage(msg: ProtoMessage, outDir: String) = {
    val data = Map("messageName" -> msg.messageName,
                   "packageName" -> msg.packageName,
                   "fields" -> msg.fields,
                   "enums" -> msg.enums,
                   "innerMessages" -> msg.innerMessages)

    val html = engine.layout("message.mustache", data)
    val filename: String = outDir + "/index.html"
    writeToFile(filename, html)
  }

  def writeToFile(path: String, contents: String) {
    val fw = new FileWriter(path)
    fw.write(contents)
    fw.close()

    Console.println("Saved ProtoDoc file to: " + path)
  }

  def allInnerMessagesOf(msg: ProtoMessage): List[ProtoMessage] = {
    var all: List[ProtoMessage] = List(msg)
    for (inner <- msg.innerMessages) {
      println("Inner message: " + msg.messageName + ">" + inner.messageName)
      all ++= allInnerMessagesOf(inner)
    }
    all
  }

  def allInnerMessagesOf(msgs: List[ProtoMessage]): List[ProtoMessage] = {
    var all: List[ProtoMessage] = List()
    for(msg <- msgs) {
      all ++= allInnerMessagesOf(msg)
    }
    all
  }

}