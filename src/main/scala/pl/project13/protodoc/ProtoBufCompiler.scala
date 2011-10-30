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
object ProtoBufCompiler extends Logger {

   
  /**
   * Parse all contents of all protocol buffer files passed in,
   * an List of ProtoMessages will be returned
   *
   * todo return something more generic, enum can also be top level
   */
  def parse(protos: String*): Seq[ProtoType] = {

    // parse
    val parsedProtos = parseNoVerify(protos:_*)

    // verify
    val verification = ProtoBufVerifier.verify(parsedProtos)

    // print errors, stop execution
    if(verification.invalid) {
      verification.errors.foreach(error(_))
    }

    // else return protos
    parsedProtos
  }

  def parseNoVerify(protos: String*) = {
    for (proto <- protos ) yield ProtoBufParser.parse(proto)
  }
  
  implicit def file2string(f: File): String = {
    Source.fromFile(f).toString()
  }
  
  implicit def files2strings(f: List[File]): List[String] = {
    f.map(file2string(_))
  }

  implicit def file2fileList(f: File): List[File] = {
    List(f)
  }
   
}