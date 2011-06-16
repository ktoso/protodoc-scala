package pl.project13.protodoc

import model.ProtoModifier
import model.ProtoModifier._
import model._
import scala.util.parsing.combinator._

/**
 * @author Konrad Malawski
 */
object ProtoBufParser extends RegexParsers with ParserConversions {

  def ID = """[a-zA-Z]([a-zA-Z0-9]|_[a-zA-Z0-9])*""".r
  def NUM = """[1-9][0-9]*""".r


  /**
   * For now, just ignore whitespaces
   */
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  def message: Parser[_ <: ProtoMessage] = "message" ~ ID ~ "{" ~ rep(enumField | messageField ) ~ "}" ^^ {
    case m ~ id ~ p1 ~ allFields ~ p2 =>

      Console.println("Parsed message '%s'".format(id))
      Console.println(" fields: " + list2typedMessageFieldList(allFields))
      Console.println(" enums: " + list2typedEnumTypeList(allFields))

      new ProtoMessage(messageName = id ,
                       packageName = "plz.change.me.im.not.real",
                       fields = allFields /*will be implicitly filtered*/,
                       enums = allFields /*will be implicitly filtered*/,
                       innerMessages = List())    // todo this is a stub
  }

  def modifier: Parser[ProtoModifier] = "optional" | "required" | "repeated" ^^ { s => ProtoModifier.REQUIRED }

  def protoType = (
                    "int"
                  | "double"
                  | "float"
                  | "int32"
                  | "int64"
                  | "uint32"
                  | "uint64"
                  | "sint32"
                  | "sint64"
                  | "fixed32"
                  | "fixed64"
                  | "sfixed32"
                  | "sfixed64"
                  | "bool"
                  | "string"
                  | "bytes"
  )

  def field = enumField | messageField

  // enums
  def enumField: Parser[ProtoEnumTypeField] = "enum" ~ ID ~ "{" ~ rep(enumValue) ~ "}" ^^ { case e ~ id ~ p1 ~ vals ~ p2 =>
      Console.println("parsing enum type '" + id + "'...")
      Console.println("enum values: " + vals)

      ProtoEnumTypeField(typeName = id, vals)
  }

  def enumValue: Parser[ProtoEnumValue] = ID ~ "=" ~ NUM ~ ";" ^^ { case id ~ eq ~ num ~ end => ProtoEnumValue(id, num) }

  // fields
  def messageField = opt(modifier) ~ protoType ~ ID ~ "=" ~ integerValue ~ opt(defaultValue) ~ ";" ^^ {
    case mod ~ pType ~ id ~ eq ~ tag ~ defaultVal ~ end =>
    Console.println("parsing message field '" + id + "'...")

    val modifier = mod.getOrElse(ProtoModifier.NONE)
    ProtoMessageField.toTypedField(pType, id, tag, modifier, defaultVal)
  }

  def defaultValue = "[" ~ "default" ~ "=" ~ (ID | NUM) ~ "]"

  // field values
  def integerValue: Parser[Int] = ("[1-9][0-9]*".r) ^^ { s => s.toInt }

  def booleanValue: Parser[Boolean] = ("true" | "false") ^^ { s => s.toBoolean }

  /* methods */

  def parse(s: String): ProtoMessage = parseAll(message, s) match {
    case Success(res, _) => res
    //    case Failure(msg, _)  => throw new RuntimeException(msg)
    //    case Error(msg, _) => throw new RuntimeException(msg)
    case x: Failure => throw new RuntimeException(x.toString())
    case x: Error => throw new RuntimeException(x.toString())
  }
}