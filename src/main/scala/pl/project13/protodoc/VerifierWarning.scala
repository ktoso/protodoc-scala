package pl.project13.protodoc

/**
 * An verifier error, which could say that for example we've introduced a field with a duplicate name etc
 * @param about which the field name of the source of this error
 * @param msg a detailed message explaining the error
 */
case class VerifierWarning(about: String, msg: String)

// todo should be a warning, not an error
case class EnumHasNoValuesVerificationWarning(override val about:String,
                                        override val msg: String = "The enum has no values")
                                        extends VerificationError(about, msg)
