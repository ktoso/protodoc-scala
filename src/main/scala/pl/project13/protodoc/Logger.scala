package pl.project13.protodoc

import model.ProtoType
import utils.AnsiCodes

trait Logger {

  var verbose = Option(System.getenv("VERBOSE")).getOrElse("true").toBoolean

  def b(msg: ProtoType) = Logger.BOLD + msg.fullName + Logger.RESET
  def b(msg: Any) = Logger.BOLD + msg.toString + Logger.RESET
  
  def info(msg: Any) {
    if (verbose) println("[INFO] " + msg)
  }

  def log(msg: Any) {
    if (verbose) println("[LOG]  " + msg)
  }

  def good(msg: Any) {
    if (verbose) println(Logger.green("[GOOD] " + msg))
  }

  def ok(msg: Any = "") {
    if (verbose) println(Logger.green("[ OK ] " + msg))
  }

  def warn(msg: Any) {
    if (verbose) println(Logger.yellow("[WARN] " + msg))
  }

  def error(msg: Any) {
    if (verbose) println(Logger.red("[ERR]  " + msg))
  }

}

object Logger extends AnsiCodes {
  implicit def any2string(any: Any): String = any.toString

  private def yellow(msg: Any) = Logger.YELLOW + msg + Logger.RESET

  private def green(msg: Any) = Logger.GREEN + msg + Logger.RESET

  private def red(msg: Any) = Logger.RED + msg + Logger.RESET
}
