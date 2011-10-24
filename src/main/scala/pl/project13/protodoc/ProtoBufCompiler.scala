package pl.project13.protodoc

import scala.io._
import exceptions.{UnknownTypeException, ProtoDocParsingException}
import model._
import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader
import java.io.File

/**
 * @author Konrad Malawski
 */
object ProtoBufCompiler extends ImplicitConversions
                           with Logger {

  /**
   * Parse all contents of all protocol buffer files passed in,
   * an List of ProtoMessages will be returned
   *
   * todo return something more generic, enum can also be top level
   */
  def parse(protos: List[File]): List[ProtoType] = {

    val parsedProtos = for (proto <- protos ) yield ProtoBufParser.parse(Source.fromFile(proto))

    ProtoBufVerifier.verify(parsedProtos)
  }

  // some ansi helpers --------------------------------------------------------
  def ANSI(value: Any) = "\u001B[" + value + "m"

  val BOLD = ANSI(1)
  val RESET = ANSI(0)
}