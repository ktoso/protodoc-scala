package pl.project13.protodoc

/**
 * An verifier error, which could say that for example we've introduced a field with a duplicate name etc
 * @param about which the field name of the source of this error
 * @param msg a detailed message explaining the error
 */
case class VerifierError(about: String, msg: String)

case class DuplicateTagVerifierError(override val about:String,
                                     details: String)
                                     extends VerifierError(about, "Protocol Buffer tags, must be unique in the range of one Type (message, enumeration)." + details)
