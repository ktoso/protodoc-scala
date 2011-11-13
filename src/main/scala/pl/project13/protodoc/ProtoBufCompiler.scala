package pl.project13.protodoc

import exceptions.{ProtoDocCompilerException, ProtoDocVerificationException, ProtoDocParsingException, UnknownTypeException}
import model._
import scala.io._
import java.io.File

/**
 * @author Konrad Malawski
 */
object ProtoBufCompiler extends Logger {

  /**
   * Parse all contents of all protocol buffer files passed in,
   * an List of ProtoMessages will be returned
   */
  def compile(protos: List[String]): List[ProtoType] = {

    // compile
    val parsedProtos = parseNoVerify(protos)
    ok("Parsed "+parsedProtos.length+" proto types...")

    // verify
    val verification = ProtoBufVerifier.verify(parsedProtos)

    // print errors, stop execution
    if(verification.invalid) {
      error("Contained errors, aborting execution")
      verification.errors.foreach(error(_))
      throw new ProtoDocVerificationException(verification)
    }

    ok("Verification finished, no errors found.")
    // return protos
    parsedProtos
  }
  
  def compile(proto: String): List[ProtoType] = compile(List(proto))

  /**
   * Should only be used when testing, in real life it's really rare to see just one me
   */
  def compileOne(proto: String): ProtoType = {
    compile(List(proto)).head
  }

  /**
   * It's like this because each String (proto file, may contain more
   * than one message, so we'll need to flatten them in the end
   */
  private def parseNoVerify(protos: List[String]) = {
    (for (proto <- protos ) yield ProtoBufParser.parse(proto)).flatten
  }

  private implicit def file2string(f: File): String = {
    Source.fromFile(f).toString()
  }

  private implicit def files2strings(f: List[File]): List[String] = {
    f.map(file2string(_))
  }

  private implicit def file2fileList(f: File): List[File] = {
    List(f)
  }

}