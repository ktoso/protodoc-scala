package pl.project13.protodoc

import model.ProtoModifier
import model.ProtoModifier._
import model._
import scala.util.parsing.combinator._

/**
 * @author Konrad Malawski
 */
object ProtoBufParser extends RegexParsers with ParserConversions {

  def ID = """[a-zA-Z]([a-zA-Z0-9]*|_[a-zA-Z0-9]*)*""".r
  def NUM = """[1-9][0-9]*""".r


  /**
   * For now, just ignore whitespaces
   */
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  def message: Parser[_ <: ProtoMessage] = "message" ~ ID ~ "{" ~ rep(enumField | messageField ) ~ "}" ^^ {
    case m ~ id ~ p1 ~ allFields ~ p2 =>

      log("Parsed message '%s'".format(id))
      log(" fields: " + list2typedMessageFieldList(allFields))
      log(" enums: " + list2typedEnumTypeList(allFields))

      new ProtoMessage(messageName = id ,
                       packageName = "plz.change.me.im.not.real",
//                       fields = messageFields,
                       fields = allFields /*will be implicitly filtered*/,
//                       enums = enumFields,
                       enums = allFields /*will be implicitly filtered*/,
                       innerMessages = List())    // todo this is a stub
  }

  def modifier: Parser[ProtoModifier] = ("optional" | "required" | "repeated") ^^ {s =>
    log(s)
    ProtoModifier.str2modifier(s)
  }

  def protoType: Parser[String] = ( "int32"
                                  | "int64"
                                  | "uint32"
                                  | "uint64"
                                  | "sint32"
                                  | "sint64"
                                  | "int"
                                  | "fixed32"
                                  | "fixed64"
                                  | "sfixed32"
                                  | "sfixed64"
                                  | "double"
                                  | "float"
                                  | "bool"
                                  | "string"
                                  | "bytes"
  )

  def anyField = enumField | messageField

  // enums
  def enumField: Parser[ProtoEnumTypeField] = "enum" ~ ID ~ "{" ~ rep(enumValue) ~ "}" ^^ {
    case e ~ id ~ p1 ~ vals ~ p2 =>
      log("parsing enum type '" + id + "'...")
      log("enum values: " + vals)

      ProtoEnumTypeField(typeName = id, vals)
  }

  def enumValue: Parser[ProtoEnumValue] = ID ~ "=" ~ NUM ~ ";" ^^ {
    case id ~ eq ~ num ~ end =>
      ProtoEnumValue(id, num)
  }

  // fields
  def messageField = opt(modifier) ~ protoType ~ ID ~ "=" ~ integerValue ~ opt(defaultValue) ~ ";" ^^ {
    case mod ~ pType ~ id ~ eq ~ tag ~ defaultVal ~ end =>
      log("parsing message anyField '" + id + "'...")

      val modifier = mod.getOrElse(NoProtoModifier())
      ProtoMessageField.toTypedField(pType, id, tag, modifier, defaultVal)
  }

  def defaultValue = "[" ~ "default" ~ "=" ~ (ID | NUM) ~ "]"

  // field values
  def integerValue: Parser[Int] = ("[1-9][0-9]*".r) ^^ {
    s =>
      s.toInt
  }

  def booleanValue: Parser[Boolean] = ("true" | "false") ^^ {
    s =>
      s.toBoolean
  }

  /* methods */

  def parse(s: String): ProtoMessage = parseAll(message, s) match {
    case Success(res, _) => res
    //    case Failure(msg, _)  => throw new RuntimeException(msg)
    //    case Error(msg, _) => throw new RuntimeException(msg)
    case x: Failure => throw new RuntimeException(x.toString())
    case x: Error => throw new RuntimeException(x.toString())
  }

  def log(msg: String) = {
    Console.println(msg)
  }
}