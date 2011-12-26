name := "ProtoDoc Scala"

organization := "pl.project13.protodoc"

version := "1.0"

scalaVersion := "2.9.1"


// main class config (also for executable jar)
mainClass in(Compile, run) := Some("pl.project13.protodoc.runner.ProtoDocMain")

mainClass in(Compile, packageBin) := Some("pl.project13.protodoc.runner.ProtoDocMain")


// repositories
resolvers += "Scarg Repository" at "http://xfire.github.com/scarg/maven-repo"

//resolvers += "Proguard plugin repo" at "http://siasia.github.com/maven2"

resolvers += "FuseSource Public Repository" at "http://repo.fusesource.com/nexus/content/repositories/public"


// cache libs in common repository (maven-like, unlike sbt 0.7.3)
retrieveManaged := true


// dependencies
libraryDependencies += "de.downgra" % "scarg_2.8.1" % "1.0.0-SNAPSHOT"

libraryDependencies += "org.fusesource.scalate" % "scalate-core" % "1.5.2"

libraryDependencies += "com.google.guava" % "guava" % "11.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

// additional classpath

unmanagedClasspath in Runtime <+= (baseDirectory) map { bd => Attributed.blank(bd / "resources") }


// proguard config

//addSbtPlugin("com.github.siasia" % "xsbt-proguard-plugin" % "0.1-SNAPSHOT")

seq(ProguardPlugin.proguardSettings: _*)

proguardOptions ++= List(keepMain("pl.project13.protodoc.runner.ProtoDocMain"),
                         "-keepclasseswithmembers public class * { public static void main(java.lang.String[]); }",
                         "-dontoptimize",
                         "-dontobfuscate",
                         "-keep class **",
                         "-dontwarn **$$anonfun$*",
                         "-dontwarn scala.collection.immutable.RedBlack$Empty",
                         "-dontwarn scala.tools.**,plugintemplate.**",
                         "-keep interface scala.ScalaObject"
)
