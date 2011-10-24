package pl.project13.protodoc.utils

/**
 * A collection of ansi color codes, to make it useful in loggers etc
 */
trait AnsiCodes {

  val BLACK = ANSI("0;30")
  val BLUE = ANSI("0;34")
  val GREEN = ANSI("0;32")
  val CYAN = ANSI("0;36")
  val RED = ANSI("0;31")
  val PURPLE = ANSI("0;35")
  val BROWN = ANSI("0;33")
  val GRAY = ANSI("0;37")
  val DARK_GRAY = ANSI("1;30")
  val LIGHT_BLUE = ANSI("1;34")
  val LIGHT_GREEN = ANSI("1;32")
  val LIGHT_CYAN = ANSI("1;36")
  val LIGHT_RED = ANSI("1;31")
  val LIGHT_PURPLE = ANSI("1;35")
  val YELLOW = ANSI("1;33")
  val WHITE = ANSI("1;37")

  val BOLD = ANSI(1)
  val RESET = ANSI(0)

  def ANSI(style: Any, value: Any) {
    ANSI(style + ";" + value)
  }

  def ANSI(value: Any) {
    "\u001B[" + value + "m"
  }
}