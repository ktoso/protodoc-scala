import java.io.{FileWriter, File}
import sbt._
import Keys._

object PluginDef extends Build {

  override def projects = Seq(root)
  lazy val root = Project("plugins", file(".")) settings(
    commands ++= Seq(updateMustaches)
  )

  // custom commands

  // update mustache files list
  def updateMustaches = Command.command("update-mustaches") { state =>

    val writer = new FileWriter("src/main/resources/file_index")

    def saveFiles(dir: File) {
      for (file <- dir.listFiles;
           if file.getName != ".directory") {
        val path = file.getAbsolutePath
        val resources = "resources/"
        val cutFrom = path.indexOf(resources)
        val cutPath = path.substring(cutFrom + resources.length, path.length)

        println("[info] Adding to index: "+cutPath) // todo, how do I get the sbt logger?
        writer.write(cutPath + "\n")
      }
    }

    saveFiles(new File("src/main/resources/js"))
    saveFiles(new File("src/main/resources/images"))
    saveFiles(new File("src/main/resources/templates"))

    writer.close()

    state
  }
}
