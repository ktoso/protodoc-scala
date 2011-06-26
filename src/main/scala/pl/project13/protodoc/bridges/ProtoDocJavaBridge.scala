package pl.project13.protodoc.bridges

import pl.project13.protodoc.ProtoBufParser
import pl.project13.protodoc.templating.ProtoDocTemplateEngine

/**
 * 
 * @author Konrad Malawski
 */

class ProtoDocJavaBridge {

  val templateEngine = new ProtoDocTemplateEngine()

  def renderMessageSite(text: String): String = {
    val msg = ProtoBufParser.parse(text)
    templateEngine.renderMessagePage(msg)
  }
}