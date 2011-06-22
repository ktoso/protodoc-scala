package pl.project13.protodoc.model

import pl.project13.protodoc.exceptions.InvalidModifierException

/**
 * @author Konrad Malawski
 */
sealed class ProtoModifier
case class OptionalProtoModifier() extends ProtoModifier
case class RequiredProtoModifier() extends ProtoModifier
case class RepeatedProtoModifier() extends ProtoModifier

object ProtoModifier {
  def str2modifier(str: String): ProtoModifier = str match {
    case "optional" =>
      OptionalProtoModifier()
    case "required" =>
      RequiredProtoModifier()
    case "repeated" =>
      RepeatedProtoModifier()
    case _ =>
      throw new InvalidModifierException("Found invalid modifier: " + str);
  }
}