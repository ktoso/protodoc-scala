import AssemblyKeys._

name := "protodoc"
version := "1.1"

// Add a single dependency
libraryDependencies += "junit" % "junit" % "4.8" % "test"

// Add multiple dependencies
libraryDependencies ++= Seq(
	"net.databinder" %% "dispatch-google" % "0.7.8",
	"net.databinder" %% "dispatch-meetup" % "0.7.8"	
)


seq(assemblySettings: _*)
