package pl.project13.protodoc.templating

import pl.project13.protodoc.model.{ProtoEnumType, ProtoMessageType, ProtoType}
import scala.annotation.tailrec
import scala.collection.JavaConversions
import scala.collection.JavaConverters._
import pl.project13.protodoc.Logger
import com.google.common.io.{Files => GFiles}
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import org.fusesource.scalate._

import Mustache._
import java.util.regex.Pattern
import java.io.{InputStreamReader, FileWriter, File}
import com.google.common.io.CharStreams
import com.google.common.base.Charsets
import java.nio.file.{StandardCopyOption, CopyOption, Paths, Files}
import javax.swing.filechooser.GenericFileSystemView

/**
 *
 * @author Konrad Malawski
 */
class ProtoDocTemplateEngine
  extends AnsiTerminalTools
  with Logger {

  ProtoDocTemplateEngine.copyTemplatesToTmp()

  var templatesDir = new File(Mustache.TemplatesDirPath)

  val engine = new TemplateEngine(List(templatesDir))

  import engine._

  layoutStrategy = new DefaultLayoutStrategy(engine, "mylayout".mustache)

  // default bindings
  bindings = List(Binding(name = "protodoc_version",
    className = "String",
    defaultValue = Option(""""v1.0"""")))

  def renderTableOfContents(contents: List[ProtoType]) = {
    Console.println("TRY TO DO TO: " + contents.mkString("[", ",", "]"))
    
    contents match {
      case msgs: List[ProtoMessageType] =>
        val allMsgs = allInnerMessagesOf(msgs)
        val allEnums = allInnerEnumsOf(msgs)
        val sum = allMsgs ++ allEnums

        val all = sum.sortBy(m => m.fullName)

        val data = Map("contents" -> all)
        layout("index".mustache, data)

      case _ =>
        throw new RuntimeException("Tried to render table of contents for " + contents + " which are not")
    }
  }

/**
 * Renders the HTML with all Messages, a table of contents so to say
 */
def renderTableOfContents (contents: List[ProtoType], outDir: String) {
val html = renderTableOfContents (contents)
val filename: String = outDir + "/index.html"
writeToFile (filename, html)
}

def renderTypePage (msg: ProtoMessageType) = {
val isInnerMessage = msg.packageName.find (c => c.isUpper)

val data = Map ("messageName" -> msg.messageName,
"packageName" -> msg.packageName,
"isInnerMessage" -> isInnerMessage,
"comment" -> msg.comment,
"fields" -> msg.fields,
"enums" -> msg.enums,
"innerMessages" -> msg.innerMessages)

engine.layout ("message".mustache, data)
}

def renderTypePage (protoType: ProtoType, outDir: String) {
protoType match {
// messages and inner-{messages, enums}
case msg: ProtoMessageType =>
val html = renderTypePage (msg)
val filename: String = outDir + "/" + msg.fullName + ".html"
writeToFile (filename, html)

msg.innerMessages.foreach (renderTypePage (_, outDir) )
msg.enums.foreach (renderEnumPage (_, outDir) )

// outer enums
case enum: ProtoEnumType =>
val html = renderEnumPage (enum)
val filename: String = outDir + "/" + enum.fullName + ".html"
writeToFile (filename, html)
}
}

def renderEnumPage (enum: ProtoEnumType) = {

import enum._

val data = Map ("enumName" -> typeName,
"packageName" -> packageName,
"comment" -> comment,
"values" -> values)

engine.layout ("enum".mustache, data)
}

def renderEnumPage (enum: ProtoEnumType, outDir: String) {
val html = renderEnumPage (enum)
val filename: String = outDir + "/" + enum.fullName + ".html"
writeToFile (filename, html)
}

def writeToFile (path: String, contents: String) {
val file = new File (path)
GFiles.createParentDirs (file)
file.createNewFile ()

val fw = new FileWriter (path)
fw.write (contents)
fw.close ()
//    info("Saved ProtoDoc file to: " + path)
}

// traversal methods

// todo @pbadenski: why not flatMap ?
def allInnerMessagesOf (m: ProtoType): List[ProtoMessageType] = {
if (m.isInstanceOf[ProtoMessageType] ) {
val msg = m.asInstanceOf[ProtoMessageType]

var all: List[ProtoMessageType] = List (msg)

val inners = msg.innerMessages
for (inner <- inners) {
info ("Inner:   " + msg.fullName + "." + BOLD + inner.messageName + RESET)
all ++= allInnerMessagesOf (inner)
}

all
} else {
Nil
}

}

def allInnerMessagesOf (msgs: List[ProtoMessageType] ): List[ProtoType] = {
msgs.flatMap (allInnerMessagesOf (_) )
}

def allInnerEnumsOf (msgs: List[ProtoMessageType] ): List[ProtoEnumType] = {
msgs.map (_.enums).flatten
}
}

object ProtoDocTemplateEngine extends Logger {

  def copyTemplatesToTmp() {
    info("Preparing templates working directory...")

    val dir = Paths.get(Mustache.TemplatesDirPath)
    if (!Files.exists(dir)) {
      Files.createDirectory(dir) // create tmp dir
    }

    val classLoader = getClass.getClassLoader
    val fileIndex = classLoader.getResourceAsStream("file_index")

    try {
      val indexSource = io.Source.fromInputStream(fileIndex)
      indexSource.getLines().foreach(fileName => {
        val fileStream = classLoader.getResourceAsStream(fileName)

        val targetFile = Paths.get(Mustache.TemplatesDirPath + "/" + fileName)

        info("Preparing file: " + targetFile)

        GFiles.createParentDirs(targetFile.toFile)
        Files.copy(fileStream, targetFile, StandardCopyOption.REPLACE_EXISTING)
      })
    } catch {
      case ex: NullPointerException => warn("Unable to copy fresh files into workspace...")
    }

    ok("Done preparing temporary working directory...")
  }
}