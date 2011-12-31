package pl.project13.protodoc

import util.parsing.combinator.{ImplicitConversions, RegexParsers}

class Term

abstract class Operation {
  def perform(a: Long, b: Long): Long
}

abstract class AOperation extends Operation
object AOperation {
  def apply(it: String) = if(it == "+") Addition() else Subtraction()
}

abstract class MOperation extends Operation
object MOperation {
  def apply(it: String) = if(it == "*") Multiplication() else Division()
}

case class Addition() extends AOperation {
 override def perform(a: Long, b: Long): Long = a + b
}
case class Subtraction() extends AOperation {
 override def perform(a: Long, b: Long): Long = a - b
}

case class Multiplication() extends MOperation {
 override def perform(a: Long, b: Long): Long = a * b
}
case class Division() extends MOperation {
 override def perform(a: Long, b: Long): Long = (a / b)
}

object MathParser extends RegexParsers with ImplicitConversions {
  def number: Parser[Long] = "[0-9]+".r ^^ { _.toLong }
  def num: Parser[Long] = lp ~> number <~ rp
  
  def add: Parser[AOperation] = ("+" | "-" ) ^^ { AOperation(_) }
  def mult: Parser[MOperation] = ("*" | "/" ) ^^ { MOperation(_) }
//  def add: Parser[Any] = ("+" | "-" )
//  def mult: Parser[Any] = ("*" | "/" )

  def op: Parser[Long] = lp ~> num ~! (add | mult) ~! num <~ rp ^^ {
    case num1 ~ op ~ num2 => op.perform(num1, num2)
  }

//  def op: Parser[Any] = lp ~> num ~ (add | mult) ~ num <~ rp
//  def all: Parser[Any] = rep(op)

  def lp: Parser[Option[String]] = opt("(")
  def rp: Parser[Option[String]] = opt(")")
  def stop: Parser[Option[String]] = opt(";")
  
  def parse(string: String): Any = parseAll(op, string) match {
    case Success(res, _) => res
    case e => throw new RuntimeException(e.toString)
  }
}

object MathParserTest extends App {

  override def main(args: Array[String]) {
    import MathParser._
    println(parse("10+15"))
    println(parse("10*2"))
  }
}