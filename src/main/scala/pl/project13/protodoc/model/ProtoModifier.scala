package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
sealed class ProtoModifier
case class NoProtoModifier() extends ProtoModifier
case class OptionalProtoModifier() extends ProtoModifier
case class RequiredProtoModifier() extends ProtoModifier
case class RepeatedProtoModifier() extends ProtoModifier

object ProtoModifier {
  def str2modifier(str: String): ProtoModifier = str match {
    case "optional" =>
      NoProtoModifier()
    case "required" =>
      RequiredProtoModifier()
    case "repeated" =>
      RepeatedProtoModifier()
    case _ =>
      println("Tried to convert modifier: " + str)
      //todo better to throw an exception here
      NoProtoModifier()
  }
}