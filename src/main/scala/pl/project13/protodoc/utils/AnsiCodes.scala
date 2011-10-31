package pl.project13.protodoc.utils

/**
 * A collection of ansi color codes, to make it useful in loggers etc
 */
trait AnsiCodes {

  val BLACK        = "\u001B[0;30m"
  val BLUE         = "\u001B[0;34m"
  val GREEN        = "\u001B[0;32m"
  val CYAN         = "\u001B[0;36m"
  val RED          = "\u001B[0;31m"
  val PURPLE       = "\u001B[0;35m"
  val BROWN        = "\u001B[0;33m"
  val GRAY         = "\u001B[0;37m"
  val DARK_GRAY    = "\u001B[1;30m"
  val LIGHT_BLUE   = "\u001B[1;34m"
  val LIGHT_GREEN  = "\u001B[1;32m"
  val LIGHT_CYAN   = "\u001B[1;36m"
  val LIGHT_RED    = "\u001B[1;31m"
  val LIGHT_PURPLE = "\u001B[1;35m"
  val YELLOW       = "\u001B[1;33m"
  val WHITE        = "\u001B[1;37m"
  val BOLD         = "\u001B[1m"
  val RESET        = "\u001B[0m"

}