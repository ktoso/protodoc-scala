package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
//object ProtoModifier extends Enumeration {
//  type ProtoModifier = Value
//  val NONE, OPTIONAL, REQUIRED, REPEATED = Value
//
//  /**
//   * todo is it possible to replace this with a
//   */
//  implicit def str2protoMod(str: String): ProtoModifier.Value = str match {
//    case "optional" =>
//      ProtoModifier.OPTIONAL
//    case "required" =>
//      ProtoModifier.REQUIRED
//    case "repeated" =>
//      ProtoModifier.REPEATED
//    case _ =>
//      ProtoModifier.NONE
//  }
//}

// todo the enumeration method caused too many problems with parser combinators