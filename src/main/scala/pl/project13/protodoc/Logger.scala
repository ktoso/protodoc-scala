package pl.project13.protodoc

trait Logger {

  var verbose = true 

  def info(msg: String) {
    if(verbose) Console.println(msg)
  }

  def log(msg: String) {
    if(verbose) Console.println(Logger.GREEN + msg + Logger.RESET)
  }

  def error(msg: String) {
    if(verbose) Console.println(msg)
  }
  
}

object Logger {
  def ANSI(style: Any, value: Any): Unit = ANSI(style + ";" + value)
  def ANSI(value: Any): Unit             = "\u001B[" + value + "m"

  val BOLD  = ANSI(1)
  val GREEN = ANSI("0;32")
  val RESET = ANSI(0)
}