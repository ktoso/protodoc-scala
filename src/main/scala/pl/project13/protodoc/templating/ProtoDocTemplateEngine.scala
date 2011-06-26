package pl.project13.protodoc.templating

import org.fusesource.scalate._
import layout.DefaultLayoutStrategy
import java.io.{FileWriter, File}
import pl.project13.protodoc.model.{ProtoEnumType, ProtoMessage}
import scala.annotation.tailrec

/**
 *
 * @author Konrad Malawski
 */
class ProtoDocTemplateEngine extends AnsiTerminalTools {

  var templatesDir = new File("/home/ktoso/coding/protodoc-scala/src/main/templates/")

  val engine = new TemplateEngine(List(templatesDir))
  engine.layoutStrategy = new DefaultLayoutStrategy(engine, "mylayout.mustache")

  // default bindings
  engine.bindings = List(Binding(name = "protodoc_version",
                                 className = "String",
                                 defaultValue = Option(""""v1.0"""")))

  def renderTableOfContents(contents: List[ProtoMessage]) = {
    val all = (allInnerMessagesOf(contents) ++ allInnerEnumsOf(contents)).sortBy(m => m.fullName)

    val data = Map("contents" -> all)

    engine.layout("/home/ktoso/coding/protodoc-scala/src/main/templates/index.mustache", data)
  }

  /**
   * Renders the HTML with all Messages, a table of contents so to say
   */
  def renderTableOfContents(contents: List[ProtoMessage], outDir: String) {
    val html = renderTableOfContents(contents)
    val filename: String = outDir + "/index.html"
    writeToFile(filename, html)
  }

  def renderMessagePage(msg: ProtoMessage) = {
    val isInnerMessage = msg.packageName.find(c => c.isUpper)

    val data = Map("messageName" -> msg.messageName,
                   "packageName" -> msg.packageName,
                   "isInnerMessage" -> isInnerMessage,
                   "comment" -> msg.comment,
                   "fields" -> msg.fields,
                   "enums" -> msg.enums,
                   "innerMessages" -> msg.innerMessages)

    engine.layout("/home/ktoso/coding/protodoc-scala/src/main/templates/message.mustache", data)
  }

  def renderMessagePage(msg: ProtoMessage, outDir: String) {
    val html = renderMessagePage(msg)
    val filename: String = outDir + "/" + msg.fullName + ".html"
    writeToFile(filename, html)

    msg.innerMessages.foreach(renderMessagePage(_, outDir))
    msg.enums.foreach(renderEnumPage(_, outDir))
  }

  def renderEnumPage(enum: ProtoEnumType) = {
    val data = Map("enumName" -> enum.typeName,
                   "packageName" -> enum.packageName,
                   "comment" -> enum.comment,
                   "values" -> enum.values)

    engine.layout("/home/ktoso/coding/protodoc-scala/src/main/templates/enum.mustache", data)
  }

  def renderEnumPage(enum: ProtoEnumType, outDir: String) {
    val html = renderEnumPage(enum)
    val filename: String = outDir + "/" + enum.fullName + ".html"
    writeToFile(filename, html)
  }

  def writeToFile(path: String, contents: String) {
    val fw = new FileWriter(path)
    fw.write(contents)
    fw.close()

//    Console.println("Saved ProtoDoc file to: " + path)
  }

  // traversal methods

  // todo @pbadenski: why not flatMap ?
  def allInnerMessagesOf(msg: ProtoMessage): List[ProtoMessage] = {
    var all: List[ProtoMessage] = List(msg)
    for (inner <- msg.innerMessages) {
      println("Inner:   " + msg.fullName + "." + BOLD + inner.messageName + RESET)
      all ++= allInnerMessagesOf(inner)
    }
    all
  }

  // todo @pbadenski: why not flatMap ?
  def allInnerMessagesOf(msgs: List[ProtoMessage]): List[ProtoMessage] = {
    var all: List[ProtoMessage] = List()
    for(msg <- msgs) {
      println("Message: " + BOLD + msg.fullName + RESET)
      all ++= allInnerMessagesOf(msg)
    }
    all
  }

  def allInnerEnumsOf(msgs: List[ProtoMessage]): List[ProtoEnumType] = {
    msgs.map(_.enums).flatten
  }
}