import sbt.Keys._
import sbt._
import xerial.sbt.Pack._

object CustomBuild extends Build {

  val NamePrefix = "com.veon.imdb.api"

  name := NamePrefix + "."

  //Sbt-Pack
  val autoSettings = Seq(crossPaths := false, packGenerateWindowsBatFile := true, packJarNameConvention := "default")

  lazy val wrapper = Project(
    id = "imdb",
    base = file("imdb")
  ).settings(Common.settings: _*)
    .settings(packAutoSettings ++ autoSettings)
    .settings(mainClass in Compile := Some("imdb.Main"))
    .settings(libraryDependencies ++= Dependencies.generalDependencies)
    .settings(fork in run := true)

  fork in run := true
}