package pl.project13.protodoc

import exceptions.{UnknownTypeException, ProtoDocParsingException}
import model._
import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

/**
 * @author Konrad Malawski
 */
object ProtoBufParser
  extends RegexParsers
  with ImplicitConversions
  with ParserConversions
  with Logger {

  val ID = """[a-zA-Z_]([a-zA-Z0-9_]*|_[a-zA-Z0-9]*)*""".r

  val NUM = """[1-9][0-9]*""".r

  val FLOAT_NUM: Parser[String] =  """(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?[fFdD]?""".r

  val CHAR = """[a-zA-Z0-9]""".r

  // lists of "known types", to allow parsing of enum fields etc
  var knownEnums: List[ProtoEnumType] = Nil
  var knownMessages: List[ProtoMessageType] = Nil

  var unresolvedTypes: List[_ <: ProtoType] = Nil

  var fieldsWithUncheckedTypes: List[ProtoMessageField] = Nil

  /**
   * For now, just ignore whitespaces
   */
//  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r
  protected override val whiteSpace = """( |\n|\t|//.*)+""".r

  def pack: Parser[String] = "package" ~> repsep(ID, ".") <~ ";" ^^ {
    case packName =>
      val joinedName = packName.mkString(".")
      info("detected package name: " + joinedName)

      joinedName
  }

  def protoFile: Parser[List[_ <: ProtoType]] = opt(pack) ~ rep(enumTypeDef | messageTypeDef) ^^ {
    case maybePack ~ allTypeDefs =>

      val pack = maybePack.getOrElse("")

      ok("Parsed proto file contained [%s] types".format(allTypeDefs.length))

      // todo prepend package to all types in here
      allTypeDefs map { protoType => protoType.moveToPackage(pack)}
  }

  def messageTypeName = "message" ~> ID

  def messageBody = "{" ~> rep(enumTypeDef | instanceField | messageTypeDef) <~ "}"

  def messageTypeDef: Parser[ProtoMessageType] = opt(comment) ~ messageTypeName ~ messageBody ^^ {
    case maybeDoc ~ id ~ allFields =>
      val comment = maybeDoc.getOrElse("")
      val pack = ""

      val processedFields = addOuterMessageInfo(id, pack, allFields) // todo this can fail

      info("Parsed message in '%s' named '%s'".format(pack, id))
      info("  fields: " + list2messageFieldList(processedFields))
      info("  enums: " + list2enumTypeList(processedFields))
      info("  inner messages: " + list2messageList(processedFields))
      info("  comment: " + comment)

      val message = new ProtoMessageType(messageName = id,
                                         packageName = pack,
                                         fields = processedFields /*will be implicitly filtered*/ ,
                                         enums = processedFields /*will be implicitly filtered*/ ,
                                         innerMessages = processedFields /*will be implicitly filtered*/)
      message.comment = comment

      message
  }

  def modifier: Parser[ProtoModifier] = ("optional" | "required" | "repeated") ^^ {
    s => ProtoModifier.str2modifier(s)
  }

  // todo make these two fields use the same list
  val primitiveTypes = List("int32", "int64", "uint32", "uint64", "sint32", "sint64", "fixed32", "fixed64",
                            "sfixed32", "sfixed64", "double", "float", "bool","string", "bytes")

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

  def userDefinedType: Parser[String] = ("""\w+""".r) ^^ {
    s =>
      info("Trying to parse '" + s + "' as user defined type name...")
//      val knownEnumNames = knownEnums.map(_.typeName)
//      info("Known enums are: " + knownEnumNames.mkString(", "))
//
//    knownEnumNames.find(_ == s).getOrElse {
//      val msg = "Unable to link '" + s + "' to any known enum Type. Accepted types are: " + knownEnumNames.mkString(", ") + "."
//      throw new UnknownTypeException(msg)
//      warn(msg) // todo maybe something better
//      warn("Adding it anyway...")
//
//      unresolvedTypes + s
//    }

    s
  }

  def anyField = enumTypeDef | instanceField //| enumField

  // enums --------------------------------------------------------------------
  def enumTypeDef: Parser[ProtoEnumType] = opt(comment) ~ "enum" ~ ID ~ "{" ~ rep(enumValue) ~ "}" ^^ {
    case maybeDoc ~ e ~ id ~ p1 ~ vals ~ p2 =>
      info("detected enum type '" + id + "'...")
      info("              values: " + vals)

      val comment = maybeDoc.getOrElse("")

      val definedEnum = ProtoEnumType(id, "", vals)
      definedEnum.comment = comment

      knownEnums ::= definedEnum
      definedEnum
  }

  def enumValue: Parser[ProtoEnumValue] = opt(comment) ~ ID ~ "=" ~ NUM <~ ";" ^^ {
    case maybeDoc ~ id ~ eq ~ num =>
      val value = ProtoEnumValue(id, num)
      val comment = maybeDoc.getOrElse("")
      info("enum value: '" + value + "', with comment: '" + comment + "'")

      value.comment = comment
      value
  }

  // comments -----------------------------------------------------------------
  // a little complicated to allow for nesting comments
  def commentStart: Parser[Any]    = ("/**" | "/*")
  def commentEnd: Parser[Any]      = "*/"
  def commentContent: Parser[Any]  = rep(chrExcept('/', '*') | '/' ~ not('*') | '*' ~ not('/') | comment)
  def comment: Parser[String] = commentStart ~> commentContent <~ commentEnd ^^ {
    case comment =>
        if(comment.toString.contains("\n")) {
          val r = """(\w|\n| |\t)""".r
          comment.asInstanceOf[List[Any]]
                 .map(_.toString)
                 .filter(s => r.findFirstIn(s).isDefined)
                 .reduceRight(_.toString + _.toString)
                 .split("\n") // zomg so ugly... (removing leading spaces from comment text)
                 .map(_.trim())
                 .mkString("\n")
        } else {
          comment.asInstanceOf[List[java.lang.Character]]
                 .map(_.toString)
                 .mkString("")
        }
  }
  def cStyleComment: Parser[String] = "//" ~> rep(commentContent) <~ "\n" ^^ {
    case comment => comment.toString()
  }

  def chrExcept(cs: Char*): Parser[Char]  = elem("chrExcept", ch => (ch != CharArrayReader.EofCh) && (cs forall (ch !=)))

  // fields -------------------------------------------------------------------
  def instanceField = opt(comment) ~ modifier ~! (protoType | userDefinedType) ~ ID ~! "=" ~! integerValue ~ opt(defaultValue) <~ ";" ^^ {
    case doc ~ mod ~ pType ~ id ~ eq ~ tag ~ defaultVal =>
      info("parsing field '" + id + "'...")
      val comment = doc.getOrElse("")

      val isPrimitiveType = primitiveTypes.contains(pType)
      if(isPrimitiveType) { // it's a primitive field
        good("Found basic ("+pType+") field: "+b(id))

        val field = ProtoMessageField.toProtoField(pType, id, tag, mod, defaultVal)
        field.comment = comment
        field
      } else {
        val isKnownEnumType = knownEnums.map(_.typeName).contains(pType)
        if (isKnownEnumType) {
          // it's an enum
          good("Found enum field for already defined enum (" + pType + ") field: " + b(id))

          val itsEnumType = knownEnums.find(p => p.typeName == pType).get

          // the default value was not set, or is in fact a valid value
          //        if(defaultValue.isInstanceOf[scala.None]){
          //          val field = ProtoMessageField.toEnumField(id, itsEnumType, tag, mod, defaultVal)
          //          field.comment = comment
          //          field
          //        } else
          if (itsEnumType.values.find {_.valueName == defaultValue}.isDefined) {
            val field = ProtoMessageField.toEnumField(id, itsEnumType, tag, mod, defaultVal)
            field.comment = comment
            field
          } else {
            val msg = "The default value [" + defaultValue + "] is not a valid value for the enum type [" + itsEnumType + "]!"

            error(msg)
            err(msg)
          }
        } else {
          val msg = "Unable to create Field instance for field: '" + id + "', type: " + pType
          warn(msg) // todo maybe something better

          unresolvedTypes + pType

          val field = ProtoMessageField.toUnresolvedField(pType, id, tag, mod, defaultVal)
          field.comment = comment
          field
        }
      }
  }

  def defaultValue: Parser[Any] = "[" ~> "default" ~> "=" ~> (ID | FLOAT_NUM | NUM | stringValue) <~ "]" ^^ {
    case value =>
      info("Default values: " + value)
      value
  }

  // field values -------------------------------------------------------------
  def integerValue: Parser[Int] = ("[1-9][0-9]*".r) ^^ { s => s.toInt }

  def stringValue: Parser[String] = "\"" ~> """\w+""".r <~ "\""

  def booleanValue: Parser[Boolean] = ("true" | "false") ^^ {
    s => s.toBoolean
  }

  /* -------------------- helper methods ----------------------------------- */

  /**
   * This method adds package information for messages and enums contained withing another message
   * so that the access path to them is reflected by their package, for example:
   * <pre>
   *   package pl.project13;
   *
   *   message Outer {
   *     message Inner {}
   *   }
   * </pre>
   * Should result in <em>Inner</em> having the package "pl.project13.Outer".
   */
  def addOuterMessageInfo(msgName: String, msgPack: String, innerFields: List[_]): List[Any] = {
    var processedFields = List[Any]()

    for(field <- innerFields) field match {
      case field: ProtoMessageType =>
        val f = field.asInstanceOf[ProtoMessageType]
        info("Adding package data to ["+f.fullName+"]")
        val packageWithOuterClass: String = msgPack + "." + msgName
        val message = ProtoMessageType(messageName = f.messageName,
                                   packageName = packageWithOuterClass,
                                   fields = f.fields,
                                   enums = addOuterMessageInfo(f.messageName,
                                                               packageWithOuterClass,
                                                               f.enums),
                                   innerMessages = addOuterMessageInfo(f.messageName,
                                                                       packageWithOuterClass,
                                                                       f.innerMessages))
        message.comment = f.comment
        processedFields ::= message
      case field: ProtoEnumType =>
        val e = field.asInstanceOf[ProtoEnumType]
        val packageWithOuterClass: String = msgPack + "." + msgName
        val protoEnumType = ProtoEnumType(typeName = e.typeName,
                                          packageName = packageWithOuterClass,
                                          values = e.values)
        protoEnumType.comment = e.comment
        processedFields ::= protoEnumType
      case f =>
        info("Ignored field while fixing packages: " + f + ", appending 'as is'.")
        processedFields ::= f
    }

    processedFields
  }

  /* ----------------- API methods ----------------------------------------- */

  /**
   * Parse the contents of one protocol buffer file
   *
   * todo return something more generic, enum can also be top level
   */
  def parse(protoContent: String): List[ProtoType] = parseAll(protoFile, protoContent) match {
    case Success(res, _) => res
    case x: Failure => throw new ProtoDocParsingException(x.toString())
    case x: Error => throw new RuntimeException(x.toString())
  }

  /**
   * Parse all contents of all protocol buffer files passed in,
   * an List of ProtoMessages will be returned
   */
  def parse(protoContents: List[String]): List[_ <: ProtoType] = protoContents.map { parse(_) }.flatten
  
  def parseOne(protoContents: String): ProtoType = {
    val protoTypes = parse(protoContents)
    if(protoTypes.length  == 1) {
      protoTypes.head
    } else {
      val msg = "Found invalid number of ProtoTypes in passed in protoContents string. Expected [1], but [" + protoTypes.length + "] found."

      error(msg)
      throw new RuntimeException(msg)
    } 
  }

}