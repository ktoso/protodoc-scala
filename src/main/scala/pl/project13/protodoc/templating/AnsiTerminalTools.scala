package pl.project13.protodoc.templating

/**
 * 
 * @author Konrad Malawski
 */
trait AnsiTerminalTools {

  def ANSI(value: Any) = "\u001B[" + value + "m"

  val BOLD = ANSI(1)
  val RESET = ANSI(0)
}