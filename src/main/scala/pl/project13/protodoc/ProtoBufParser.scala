package pl.project13.protodoc

import model._
import scala.util.parsing.combinator._

/**
 * @author Konrad Malawski
 */
object ProtoBufParser extends RegexParsers with ParserConversions {

  /**
   * Defines if log messages should be printed or not
   */
  var verbose = false

  def ID = """[a-zA-Z]([a-zA-Z0-9]*|_[a-zA-Z0-9]*)*""".r

  def NUM = """[1-9][0-9]*""".r

  def CHAR = """[a-zA-Z0-9]""".r

  /**
   * For now, just ignore whitespaces
   */
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  def pack: Parser[String] = "package" ~ repsep(ID, ".") ~ ";" ^^ {
    case p ~ packName ~ end =>
      val joinedName = packName.reduceLeft(_ + "." + _)
      log("detected package name: " + joinedName)

      joinedName
  }

  def message: Parser[_ <: ProtoMessage] = opt(pack) ~ "message" ~ ID ~ "{" ~ rep(enumField | messageField) ~ "}" ^^ {
    case maybePack ~ m ~ id ~ p1 ~ allFields ~ p2 =>

      val pack = maybePack.getOrElse("")

      log("Parsed message in '%s' named '%s'".format(pack, id))
      log(" fields: " + list2messageFieldList(allFields))
      log(" enums: " + list2enumTypeList(allFields))
      log(" inner messages: " + list2messageList(allFields))

      new ProtoMessage(messageName = id,
                       packageName = pack,
                       fields = allFields /*will be implicitly filtered*/ ,
                       enums = allFields /*will be implicitly filtered*/ ,
                       innerMessages = allFields /*will be implicitly filtered*/)
  }

  def modifier: Parser[ProtoModifier] = ("optional" | "required" | "repeated") ^^ {
    s =>
      log("Found " + s + " field")
      ProtoModifier.str2modifier(s)
  }

  def protoType: Parser[String] = ("int32"
    | "int64"
    | "uint32"
    | "uint64"
    | "sint32"
    | "sint64"
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
      log("detected enum type '" + id + "'...")
      log("              values: " + vals)

      ProtoEnumTypeField(typeName = id, vals)
  }

  def enumValue: Parser[ProtoEnumValue] = ID ~ "=" ~ NUM ~ ";" ^^ {
    case id ~ eq ~ num ~ end =>
      ProtoEnumValue(id, num)
  }

  def protoDocComment: Parser[ProtoDocComment] = "/**" ~ rep(CHAR) ~ "*/" ^^ {
    case s ~ text ~ end =>
      val wholeComment = text.reduceLeft(_ + _)

      log("detected comment: '" + wholeComment + "'")
      ProtoDocComment(wholeComment)
  }

  // fields
  def messageField = opt(protoDocComment) ~ opt(modifier) ~ protoType ~ ID ~ "=" ~ integerValue ~ opt(defaultValue) ~ ";" ^^ {
    case doc ~ mod ~ pType ~ id ~ eq ~ tag ~ defaultVal ~ end =>
      log("parsing message field '" + id + "'...")

      val modifier = mod.getOrElse(RequiredProtoModifier()) // todo remove this
      ProtoMessageField.toTypedField(pType, id, tag, modifier, defaultVal)
  }

  def defaultValue: Parser[Any] = "[" ~ "default" ~ "=" ~ (ID | NUM | stringValue) ~ "]" ^^ {
    case b ~ d ~ eq ~ value ~ end =>
      log("Default values: " + value)
      value
  }

  // field values
  def integerValue: Parser[Int] = ("[1-9][0-9]*".r) ^^ {
    s =>
      s.toInt
  }

  def stringValue: Parser[String] = "\"" ~ """\w+""".r ~ "\"" ^^ {
    case o ~ stringValue ~ end =>
      stringValue
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
    if(verbose){
      Console.println(msg)
    }
  }
}