package pl.project13.protodoc.runner

import de.downgra.scarg._

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
  ! "-v" | "--verbose"   |% "active verbose output, [default = false]"       |> "verbose"
  ("-" >>> 50)
  + "proto_dir"          |% "directory containing proto files to parse"      |> 'proto_dir
  + "out_dir"            |% "output directory for the protodoc html webpage" |> 'out_dir
}


object ProtoDocMain {

  def main(args: Array[String]) = {
    ArgumentsParser().parse(args) match {
      case Right(c) =>
        println("verbose: " + c.verbose)
        println("proto_dir: " + c.proto_dir)
        println("out_dir: " + c.out_dir)
        // todo run protodoc
//      case Right(xs) =>
      case _ =>

    }
  }

  // Some ANSI helpers...
  def ANSI(value: Any) = "\u001B[" + value + "m"

  val BOLD = ANSI(1)
  val RESET = ANSI(0)

}
