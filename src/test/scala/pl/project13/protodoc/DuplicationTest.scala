package pl.project13.protodoc

import exceptions.ProtoDocVerificationException
import model._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{GivenWhenThen, Spec, FlatSpec}

/**
 *
 * @author Konrad Malawski
 */
class DuplicationTest
  extends Spec
  with GivenWhenThen
  with ShouldMatchers {

  describe("Duplicated message") {
    it ("should be detected and an Duplication error should be returned") {
      evaluating {
        ProtoBufCompiler.compile("""
          package duplicate;

          message Wiadomosc {}
          message Wiadomosc {}
        """)
      } should produce[ProtoDocVerificationException]
    }
  }

  describe("Duplicated enumeration") {
    it ("should be detected and an Duplication error should be returned") {
      evaluating {
        ProtoBufCompiler.compile("""
          package duplicate;

          enum Enumeracja {}
          enum Enumeracja {}
        """)
      } should produce[ProtoDocVerificationException]
    }
  }

}