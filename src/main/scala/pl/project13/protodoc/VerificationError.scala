package pl.project13.protodoc

/**
 * An verifier error, which could say that for example we've introduced a field with a duplicate name etc
 * @param about which the field name of the source of this error
 * @param msg a detailed message explaining the error
 */
case class VerificationError(about: String, msg: String) {
  override def toString = "Error on field ["+about+"]: "+msg
}

case class UndefinedTypeVerificationError(override val about: String, details: String)
  extends VerificationError(about, "Unable to resolve this type. Maybe you simply forgot to import it? "+details)

case class TagVerificationError(override val about: String, override val msg: String)
  extends VerificationError(about, msg)

case class DuplicateTagVerificationError(override val about: String, details: String)
  extends TagVerificationError(about, "Protocol Buffer tags, must be unique in the range of one Type (message, enumeration). "+details)
