package pl.project13.protodoc.utils

trait CommonImplicits {
  implicit def symbol2string(s: Symbol) = s.toString()
}