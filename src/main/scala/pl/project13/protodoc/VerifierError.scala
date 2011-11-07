package pl.project13.protodoc

/**
 * An verifier error, which could say that for example we've introduced a field with a duplicate name etc
 * @param about which the field name of the source of this error
 * @param msg a detailed message explaining the error
 */
case class VerifierError(about: String, msg: String)

case class UndefinedTypeVerifierError(override val about: String, details: String)
  extends VerifierError(about, "Unable to resolve this type. Maybe you simply forgot to import it? "+details)

case class TagVerifierError(override val about: String, override val msg: String)
  extends VerifierError(about, msg)

case class DuplicateTagVerifierError(override val about: String, details: String)
  extends TagVerifierError(about, "Protocol Buffer tags, must be unique in the range of one Type (message, enumeration). "+details)
