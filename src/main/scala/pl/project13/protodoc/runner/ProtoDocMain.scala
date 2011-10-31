package pl.project13.protodoc.runner

import de.downgra.scarg._
import pl.project13.protodoc.templating.ProtoDocTemplateEngine
import io.Source
import java.io.{FilenameFilter, File, FileWriter}
import java.lang.{Boolean => JBoolean}
import pl.project13.protodoc.{ProtoBufCompiler, ProtoBufParser}
import pl.project13.protodoc.model._
import _root_.pl.project13.protodoc.Logger

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
  +"proto_dir" |% "directory containing proto files to compile" |> 'proto_dir
  +"out_dir" |% "output directory for the protodoc html webpage" |> 'out_dir
}


object ProtoDocMain extends Logger {

  val templateEngine = new ProtoDocTemplateEngine

  var allParsedProtos: List[ProtoType] = List()

  val endingWithProto = new FilenameFilter() {
    override def accept(dir: File, name: String) = name.endsWith(".proto")
  }

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

    // todo will have to be changed, compiler should get all files
    for (file <- new File(protoDir).listFiles(endingWithProto)) {
      info("Parsing file: " + strong(file))

      val protoString = Source.fromFile(file).mkString

      val parsedProtos = ProtoBufCompiler.compile(protoString)

      allParsedProtos ++= parsedProtos

      parsedProtos.foreach(templateEngine.renderTypePage(_, outDir))
    }

    templateEngine.renderTableOfContents(allParsedProtos, outDir)
  }

}
