package pl.project13.protodoc

// todo should be 'about' a field, enum etc etc
/**
 * An verifier error, which could say that for example we've introduced a field with a duplicate name etc
 */
case class VerifierError(about: String, msg: String)
