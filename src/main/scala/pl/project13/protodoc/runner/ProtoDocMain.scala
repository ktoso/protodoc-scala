package pl.project13.protodoc.runner

import de.downgra.scarg._
import pl.project13.protodoc.ProtoBufParser
import pl.project13.protodoc.templating.ProtoDocTemplateEngine
import io.Source
import java.io.{File, FileWriter}
import pl.project13.protodoc.model.ProtoMessage

// we want to store three values, a boolean and two strings
class Configuration(m: ValueMap) extends ConfigMap(m) {
  val verbose = ("verbose", false).as[Boolean]
  val proto_dir = ("proto_dir", ".").as[String]
  val out_dir = ("out_dir", "").as[String]
}

// our argument parser which uses a factory to create our Configuration
case class ArgumentsParser() extends ArgumentParser(new Configuration(_))
                                     with DefaultHelpViewer {

  override val programName = Some("ProtoDoc")

  // define our expected arguments
  !"-v" | "--verbose" |% "active verbose output, [default = false]" |> "verbose"
  ("-" >>> 50)
  +"proto_dir" |% "directory containing proto files to parse" |> 'proto_dir
  +"out_dir" |% "output directory for the protodoc html webpage" |> 'out_dir
}


object ProtoDocMain {

  val templateEngine = new ProtoDocTemplateEngine()

  var parsedProtos: List[ProtoMessage] = List()

  def main(args: Array[String]) {
    ArgumentsParser().parse(args) match {
      case Right(c) =>
        println("verbose: " + c.verbose)
        println("proto_dir: " + c.proto_dir)
        println("out_dir: " + c.out_dir)

        generateProtoDoc(c.proto_dir, c.out_dir, c.verbose)
      case _ =>

    }
  }

  def generateProtoDoc(protoDir: String, outDir: String, verbose: Boolean) {
    ProtoBufParser.verbose = verbose;

    for (file <- new File(protoDir).listFiles) {
      val protoString = Source.fromFile("file.txt").mkString

      val parsedProto = ProtoBufParser.parse(protoString)
      parsedProtos ::= parsedProto
      val pageHtml = templateEngine.renderMessagePage(parsedProto)

      val outFilename = outDir + "/" + parsedProto.fullName + ".html"
      writeToFile(outFilename, pageHtml)
    }

    val indexHtml  = templateEngine.renderTableOfContents(parsedProtos)
    val indexFilename = outDir + "/index.html"
    writeToFile(indexFilename, indexHtml)
  }

  def writeToFile(path: String, contents: String) {
    val fw = new FileWriter(path)
    fw.write(contents)
    fw.close()
  }

  // Some ANSI helpers...
  def ANSI(value: Any) = "\u001B[" + value + "m"

  val BOLD = ANSI(1)
  val RESET = ANSI(0)

}
