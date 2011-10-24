package pl.project13.protodoc

import utils.AnsiCodes

trait Logger {

  var verbose = true

  def info(msg: Any) {
    if (verbose) Console.println(msg)
  }

  def log(msg: Any) {
    info(msg)
  }

  def good(msg: Any) {
    if (verbose) Console.println(Logger.green(msg))
  }

  def error(msg: Any) {
    if (verbose) Console.println(Logger.red(msg))
  }

}

object Logger extends AnsiCodes {
  implicit def any2string(any: Any): String = any.toString

  private def green(msg: Any) = Logger.RED + msg + Logger.RESET

  private def red(msg: Any) = Logger.RED + msg + Logger.RESET
} 