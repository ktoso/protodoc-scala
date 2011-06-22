import java.io.File
import sbt._
import FileUtilities._

class ProtoDocProject(info: ProjectInfo) extends DefaultProject(info) {

  //project name
  override val artifactID = "protodoc"

  //program entry point
  override def mainClass: Option[String] = Some("pl.project13.protodoc.runner.ProtoDocMain")

  val libraryJarPath = outputPath / "lib"

  override val manifestClassPath = Some(List("scalate-core-1.4.1.jar",
                                             "scarg-core_2.8.1-1.0.0-SNAPSHOT.jar",
                                             "scala-library.jar")
                                        .map( "lib/" + _)
                                        .reduceLeft(_ + " " +_)
                                       )

  def collectJarsTask = {
    val jars = mainDependencies.libraries +++ mainDependencies.scalaJars
    FileUtilities.copyFlat(jars.get, libraryJarPath, log)
  }

  lazy val pack = packageAction
//  lazy val compile = compileAction

  lazy val collectJars = task {
    collectJarsTask; None
  } dependsOn (compile)

  lazy val `jar` = task {
    None
  } dependsOn (collectJars)

//  lazy val dist = task {
//    createDirectory("dist", log)
//    copyFile(List(new File("target/scala-2.8.1/protodoc-1.0.jar")), "dist", log)
//    copyFile(List(new File("target/scala-2.8.1/lib")), "dist", log)
//    None
//  } dependsOn (compile, pack)

  //proguard
  /******** Proguard *******/
  //  lazy val outputJar = outputPath / (name + "-" + version + "-standalone.jar")
  //  val proguardJar = "net.sf.proguard" % "proguard" % "4.3" % "tools->default"
  //  val toolsConfig = config("tools")
  //
  //  def rootProjectDirectory = rootProject.info.projectPath
  //
  //  val proguardConfigurationPath: Path = outputPath / "proguard.pro"
  //  lazy val proguard = proguardTask dependsOn (`package`, writeProguardConfiguration)
  //  private lazy val writeProguardConfiguration = writeProguardConfigurationTask dependsOn `package`
  //  //lazy val pack = packTask dependsOn(proguard)
  //  private def proguardTask = task {
  //                                    FileUtilities.clean(outputJar :: Nil, log)
  //                                    val proguardClasspathString = Path.makeString(managedClasspath(toolsConfig).get)
  //                                    val configFile = proguardConfigurationPath.toString
  //                                    val exitValue = Process("java", List("-Xmx256M", "-cp", proguardClasspathString, "proguard.ProGuard", "@" + configFile)) ! log
  //                                    if (exitValue == 0) {
  //                                      None
  //                                    } else {
  //                                      Some("Proguard failed with nonzero exit code (" + exitValue + ")")
  //                                    }
  //                                  }
  //
  //  //  override def proguardOptions = List(
  //  //    "-keepclasseswithmembers public class * { public static void main(java.lang.String[]); }",
  //  //    "-dontoptimize",
  //  //    "-dontobfuscate",
  //  //    proguardKeepLimitedSerializability,
  //  //    proguardKeepAllScala,
  //  //    "-keep interface scala.ScalaObject"
  //  //  )
  //  //  override def proguardInJars = Path.fromFile(scalaLibraryJar) +++  ("lib") * "*.jar") +++ super.proguardInJars
  //  private def writeProguardConfigurationTask =
  //    task {
  //           /* the template for the proguard configuration file
  //           * You might try to remove "-keep class *" and "-keep class *", but this might break dynamic classloading.
  //           */
  //           val outTemplate = """
  //                                |-dontskipnonpubliclibraryclasses
  //                                |-dontskipnonpubliclibraryclassmembers
  //                                |-dontoptimize
  //                                |-dontobfuscate
  //                                |-dontshrink
  //                                |-dontpreverify
  //                                |-dontnote
  //                                |-dontwarn
  //                                |-libraryjars %s
  //                                |%s
  //                                |-outjars %s
  //                                |-ignorewarnings
  //                                |-keep class *
  //                                |-keep class %s { *** main(...); }
  //                                |"""
  //
  //           val defaultJar = (outputPath / defaultJarName).asFile.getAbsolutePath
  //           log.debug("proguard configuration using main jar " + defaultJar)
  //           val externalDependencies = Set() ++ (
  //             mainCompileConditional.analysis.allExternals ++ compileClasspath.get.map { _.asFile }
  //           ) map { _.getAbsoluteFile } filter { _.getName.endsWith(".jar") }
  //           def quote(s: Any) = '"' + s.toString + '"'
  //           log.debug("proguard configuration external dependencies: \n\t" + externalDependencies.mkString("\n\t"))
  //           // partition jars from the external jar dependencies of this project by whether they are located in the project directory
  //           // if they are, they are specified with -injars, otherwise they are specified with -libraryjars
  //           val (externalJars, libraryJars) = externalDependencies.toList.partition(jar => Path.relativize(rootProjectDirectory, jar).isDefined)
  //           log.debug("proguard configuration library jars locations: " + libraryJars.mkString(", "))
  //           // exclude properties files and manifests from scala-library jar
  //           val inJars = (quote(defaultJar) :: externalJars.map(quote(_) + "(!META-INF/**,!*.txt)")).map("-injars " + _).mkString("\n")
  //           val proguardConfiguration = outTemplate.stripMargin.format(libraryJars.map(quote).mkString(File.pathSeparator), inJars, quote(outputJar.absolutePath), mainClass.get)
  //           log.debug("Proguard configuration written to " + proguardConfigurationPath)
  //           FileUtilities.write(proguardConfigurationPath.asFile, proguardConfiguration, log)
  //         }
}
