package pl.project13.protodoc.integration

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{GivenWhenThen, Spec}
import pl.project13.protodoc.model.ProtoTagConversions
import pl.project13.protodoc.runner.ProtoDocMain

class FullIntegrationTest
  extends Spec
  with ShouldMatchers
  with GivenWhenThen
  with ProtoTagConversions {

  describe("ProtoDoc") {
    it("should not fail for simple proto files"){
      given("the simple/ director, with proto files")
      val source = "src/main/proto/simple"

      and("a valid destination directory")
      val dest = "target/protodoc"

      when("the files are parsed")
      ProtoDocMain.generateProtoDoc(source, dest, true)

      then("no exception should be thrown")
      and("the output should be a valid doc")
    }
  }
}