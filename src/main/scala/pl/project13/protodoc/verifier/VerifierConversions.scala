package pl.project13.protodoc.verifier

object VerifierConversions {
  implicit def string2protoTypeLookup(str: String) = new ProtoTypeLookup(str)
}