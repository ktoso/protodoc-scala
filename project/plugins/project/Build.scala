import java.io.{FileWriter, File}
import sbt._
import Keys._

object PluginDef extends Build {
  override def projects = Seq(root)
  lazy val root = Project("plugins", file(".")) dependsOn(proguard)
  lazy val proguard = uri("git://github.com/siasia/xsbt-proguard-plugin.git")

  lazy val plugins = Project("plugins", file("."))
    .dependsOn(uri("git://github.com/guardian/sbt-teamcity-test-reporting-plugin.git#1.1"))
}
